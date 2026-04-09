import os
import sys

# 🔥 FFMPEG PATH FIX
os.environ["PATH"] = r"C:\ffmpeg\ffmpeg-8.1-essentials_build\bin;" + os.environ["PATH"]

import whisper

model = whisper.load_model("base")


def format_time(seconds):
    hrs = int(seconds // 3600)
    mins = int((seconds % 3600) // 60)
    secs = int(seconds % 60)
    millis = int((seconds - int(seconds)) * 1000)
    return f"{hrs:02}:{mins:02}:{secs:02}.{millis:03}"


def generate_vtt(video_path):
    print(f"Processing: {video_path}")

    result = model.transcribe(video_path, fp16=False)

    vtt_path = video_path.replace(".mp4", ".vtt")

    with open(vtt_path, "w", encoding="utf-8") as f:
        f.write("WEBVTT\n\n")

        for seg in result["segments"]:
            f.write(f"{format_time(seg['start'])} --> {format_time(seg['end'])}\n")
            f.write(f"{seg['text']}\n\n")

    print(f"Subtitle created: {vtt_path}")


# 🔥 ENTRY POINT
if __name__ == "__main__":
    if len(sys.argv) < 2:
        print("No video path provided ❌")
    else:
        generate_vtt(sys.argv[1])