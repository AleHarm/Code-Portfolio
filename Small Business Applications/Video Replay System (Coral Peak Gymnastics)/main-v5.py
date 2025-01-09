import os
import sys
import tkinter as tk
from tkinter import ttk
import cv2
from collections import deque


def resource_path(relative_path):
    """Get the absolute path to a resource, works for both development and packaged app."""
    try:
        # PyInstaller creates a temp folder and stores the path to it in _MEIPASS
        base_path = sys._MEIPASS
    except Exception:
        base_path = os.path.abspath(".")
    
    return os.path.join(base_path, relative_path)


class DelayedWebcamApp:
    def __init__(self, root):
        self.root = root
        self.root.title("CPG Webcam")
        
        # Use resource_path to access the icon in packaged app
        icon_path = resource_path('VideoCameraIcon.ico')  # Access icon after packaging

        try:
            self.root.iconbitmap(icon_path)  # Try setting the icon as bitmap (for .ico files)
        except Exception as e:
            print(f"Error setting icon: {e}")
        
        # Default delay in seconds
        self.delay_seconds = 2.0

        # Frame rate estimation
        self.fps = 30  # Default FPS if not obtained dynamically

        # Frame buffer
        self.frame_buffer = deque()

        # Flag to indicate delay adjustment
        self.adjusting_delay = False

        # Create the settings interface
        self.create_settings_ui()

        # Webcam initialization
        self.cap = None

        # Running flag to control the loop
        self.running = False

    def create_settings_ui(self):
        # Label for delay
        ttk.Label(self.root, text="Delay (seconds):").grid(row=0, column=0, padx=10, pady=10)

        # Input for delay
        self.delay_var = tk.DoubleVar(value=self.delay_seconds)
        self.delay_input = ttk.Entry(self.root, textvariable=self.delay_var, width=10)
        self.delay_input.grid(row=0, column=1, padx=10, pady=10)

        # Button to start the webcam feed
        self.start_button = ttk.Button(self.root, text="Start Webcam", command=self.start_webcam)
        self.start_button.grid(row=1, column=0, pady=10)

        # Button to stop the webcam feed
        self.stop_button = ttk.Button(self.root, text="Stop Webcam", command=self.stop_webcam)
        self.stop_button.grid(row=1, column=1, pady=10)

        # Status message
        self.status_label = ttk.Label(self.root, text="", foreground="blue")
        self.status_label.grid(row=2, column=0, columnspan=2, pady=10)

        # Quit button
        self.quit_button = ttk.Button(self.root, text="Quit", command=self.quit_app)
        self.quit_button.grid(row=3, column=0, columnspan=2, pady=10)

    def start_webcam(self):
        self.running = True
        self.delay_seconds = self.delay_var.get()

        # Open the webcam if not already opened
        if self.cap is None:
            self.cap = cv2.VideoCapture(0)
            if not self.cap.isOpened():
                print("Error: Could not open the webcam.")
                return

            # Set resolution to 1920x1080 for high-quality video
            self.cap.set(cv2.CAP_PROP_FRAME_WIDTH, 1920)
            self.cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 1080)

            # Estimate the FPS
            self.fps = int(self.cap.get(cv2.CAP_PROP_FPS)) or 30

        # Indicate that the webcam is building frames
        self.status_label.config(text="Webcam started, please wait")
        self.adjusting_delay = True

        # Start the video loop
        self.video_loop()

    def stop_webcam(self):
        self.running = False

        # Release the webcam and close the video feed window
        if self.cap is not None:
            self.cap.release()
            self.cap = None
        cv2.destroyAllWindows()

        # Clear the frame buffer
        self.frame_buffer.clear()

        # Reset status label
        self.status_label.config(text="")

    def video_loop(self):
        if not self.running:
            return

        ret, frame = self.cap.read()

        if not ret:
            print("Error: Could not read frame.")
            self.quit_app()
            return

        # Update the delay dynamically
        new_delay = self.delay_var.get()
        if new_delay != self.delay_seconds:
            # Close webcam view temporarily while updating delay
            self.frame_buffer.clear()
            cv2.destroyAllWindows()
            self.status_label.config(text="")  # No message when adjusting delay
            self.delay_seconds = new_delay
            self.adjusting_delay = True
            return  # Exit loop temporarily

        frame_delay = int(self.fps * self.delay_seconds)

        # Ensure the buffer can handle the updated delay
        self.frame_buffer = deque(self.frame_buffer, maxlen=frame_delay)

        # Add the current frame to the buffer
        self.frame_buffer.append(frame)

        # If the buffer has enough frames for the delay, show the oldest frame
        if len(self.frame_buffer) == frame_delay:
            self.adjusting_delay = False
            self.status_label.config(text="")  # Reset message once delay is applied
            delayed_frame = self.frame_buffer.popleft()
            cv2.imshow('Delayed Webcam Feed', delayed_frame)

        # Otherwise, don't show anything but keep the webcam processing

        # Check for quit ('q') key
        if cv2.waitKey(1) & 0xFF == ord('q'):
            self.quit_app()
            return

        # Schedule the next frame update
        self.root.after(10, self.video_loop)

    def quit_app(self):
        self.running = False

        # Release webcam and close all windows
        if self.cap is not None:
            self.cap.release()
        cv2.destroyAllWindows()
        self.root.quit()


if __name__ == "__main__":
    root = tk.Tk()
    app = DelayedWebcamApp(root)
    root.mainloop()
