import tkinter as tk
from tkinter import messagebox
import numpy as np
import os
import pickle
from tkinter import font

# Find the first available .pkl file in the current directory
pkl_files = [file for file in os.listdir('.') if file.endswith('.pkl')]
if not pkl_files:
    raise FileNotFoundError("No .pkl file found in the current directory.")

model_filename = pkl_files[0]  # Use the first .pkl file
print(f"Loading model from: {model_filename}")

# Load the model
with open(model_filename, 'rb') as file:
    mlp_model = pickle.load(file)

def flip_board_for_ai(board):
    return board[7:13] + [board[13]] + board[0:6] + [board[6]]

class MancalaGame:
    def __init__(self, master):
        self.master = master
        self.master.title("Mancala")
        
        self.board = [4] * 6 + [0] + [4] * 6 + [0]  # Index 6 and 13 are stores
        
        self.buttons = []
        self.store_labels = []
        self.player_turn = True
        self.flash_count = 0
        self.create_board()
        self.update_board()
        
        # Define a font with larger size
        label_font = tk.font.Font(size=40)  # Adjust size as needed
        
        # AI move display with increased size
        self.ai_move_label = tk.Label(self.master, text="AI Move: ", width=30, height=3, font=label_font, relief="ridge", bg="lightblue")
        self.ai_move_label.grid(row=2, column=0, columnspan=8, padx=10, pady=10)


    def create_board(self):
        width = 5
        holeHeight = 2
        storeHeight = 3 * holeHeight

        # Create a common font for all elements
        common_font = font.Font(size=40)  # Adjust size as needed

        # AI's store (left side)
        ai_store = tk.Label(self.master, text="0", width=width, height=storeHeight, relief="ridge", bg="lightgray", font=common_font)
        ai_store.grid(row=0, column=0, rowspan=2, padx=5, pady=5)
        self.store_labels.append(ai_store)

        # AI's pits (top row)
        for i in range(12, 6, -1):
            btn = tk.Button(self.master, text="", width=width, height=holeHeight, state=tk.DISABLED, font=common_font)
            btn.grid(row=0, column=13-i)
            self.buttons.append(btn)

        # Player's pits (bottom row)
        for i in range(6):
            btn = tk.Button(self.master, text="", width=width, height=holeHeight, command=lambda i=i: self.player_move(i), font=common_font)
            btn.grid(row=1, column=i+1)
            self.buttons.append(btn)

        # Player's store (right side)
        player_store = tk.Label(self.master, text="0", width=width, height=storeHeight, relief="ridge", bg="lightgray", font=common_font)
        player_store.grid(row=0, column=7, rowspan=2, padx=5, pady=5)
        self.store_labels.append(player_store)


    def update_board(self):
        for i in range(6):
            self.buttons[i]['text'] = str(self.board[12-i])
        for i in range(6):
            self.buttons[i+6]['text'] = str(self.board[i])
        self.store_labels[0]['text'] = str(self.board[13])  # AI's store
        self.store_labels[1]['text'] = str(self.board[6])   # Player's store

    def player_move(self, index):
        if self.player_turn and self.board[index] > 0:
            last_pit, affected_pits, capture_pit = self.make_move(index, is_player=True)
            self.update_board()
            if capture_pit is not None:
                self.flash_capture(capture_pit)
            if last_pit == 6:
                self.flash_store(is_player=True)
                if self.check_game_over():
                    self.end_game()
            else:
                self.player_turn = False
                self.master.after(1000, self.ai_move)

    def make_move(self, index, is_player):
        stones = self.board[index]
        self.board[index] = 0
        i = index
        affected_pits = []
        capture_pit = None
        
        while stones > 0:
            i = (i + 1) % 14
            if (is_player and i == 13) or (not is_player and i == 6):
                continue
            self.board[i] += 1
            stones -= 1
            affected_pits.append(i)
        
        # Implement capture rule only for corresponding turns
        if is_player and i < 6 and self.board[i] == 1 and self.board[12-i] > 0:
            # Player captures from AI's side
            self.board[6] += self.board[i] + self.board[12-i]
            self.board[i] = 0
            self.board[12-i] = 0
            capture_pit = 12-i
        elif not is_player and 7 <= i < 13 and self.board[i] == 1 and self.board[12-i] > 0:
            # AI captures from Player's side
            self.board[13] += self.board[i] + self.board[12-i]
            self.board[i] = 0
            self.board[12-i] = 0
            capture_pit = 12-i
        
        return i, affected_pits, capture_pit

    def ai_move(self):
        if not self.player_turn:
            flipped_board = flip_board_for_ai(self.board)
            X_input = np.array([flipped_board])
            probabilities = mlp_model.predict_proba(X_input)[0]

            valid_move_found = False
            for choice in np.argsort(probabilities)[::-1]:
                if 0 <= choice < 6 and flipped_board[choice] > 0:
                    original_choice = 7 + choice
                    last_pit, affected_pits, capture_pit = self.make_move(original_choice, is_player=False)
                    self.highlight_moves(affected_pits)
                    self.ai_move_label.config(text=f"AI Move: Pit {choice + 1}")
                    valid_move_found = True
                    break
            
            if not valid_move_found:
                self.end_game()
                return

            self.update_board()
            if capture_pit is not None:
                self.flash_capture(capture_pit)

            if last_pit == 13:
                self.flash_store(is_player=False)
                if self.check_game_over():
                    self.end_game()
                else:
                    self.master.after(1000, self.ai_move)
            else:
                self.player_turn = True

    def highlight_moves(self, affected_pits):
        for i in affected_pits:
            if i < 6:
                self.buttons[i+6].config(bg="yellow")
            elif 7 <= i < 13:
                self.buttons[12-i].config(bg="yellow")
        self.master.after(1000, self.reset_highlights)

    def reset_highlights(self):
        for button in self.buttons:
            button.config(bg="SystemButtonFace")

    def flash_store(self, is_player):
        store_index = 1 if is_player else 0
        original_color = self.store_labels[store_index].cget("bg")
        
        def flash():
            if self.flash_count < 4:
                new_color = "green" if self.flash_count % 2 == 0 else original_color
                self.store_labels[store_index].config(bg=new_color)
                self.flash_count += 1
                self.master.after(250, flash)
            else:
                self.flash_count = 0

        flash()

    def flash_capture(self, pit):
        button_index = pit if pit < 6 else 12 - pit
        original_color = self.buttons[button_index].cget("bg")
        
        def flash():
            self.buttons[button_index].config(bg="green")
            self.master.after(500, lambda: self.buttons[button_index].config(bg=original_color))

        flash()

    def check_game_over(self):
        player_empty = all(self.board[i] == 0 for i in range(6))
        ai_empty = all(self.board[i] == 0 for i in range(7, 13))
        return player_empty or ai_empty

    def end_game(self):
        for i in range(6):
            self.board[6] += self.board[i]
            self.board[i] = 0
        for i in range(7, 13):
            self.board[13] += self.board[i]
            self.board[i] = 0
        
        self.update_board()
        
        winner_message = self.determine_winner()
        messagebox.showinfo("Game Over", winner_message)

    def determine_winner(self):
        if self.board[6] > self.board[13]:
            return "Game ended. Player wins!"
        elif self.board[13] > self.board[6]:
            return "Game ended. AI wins!"
        else:
            return "Game ended. It's a tie!"

# Create the game GUI
root = tk.Tk()
game = MancalaGame(root)
root.mainloop()