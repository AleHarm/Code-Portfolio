import copy
import csv
import os
import random
import threading
import time
import sys

class Mancala:
    def __init__(self):
        self.board = [4] * 6 + [0] + [4] * 6 + [0]  # 6 pits + 1 store per player
        self.player_turn = 1  # 0 = player 1, 1 = player 2

    def is_game_over(self):
        return sum(self.board[:6]) == 0 or sum(self.board[7:13]) == 0

    def get_valid_moves(self):
        return [i for i in range(6) if self.board[i + 7 * self.player_turn] > 0]

    def make_move(self, pit):
        index = pit + 7 * self.player_turn
        stones = self.board[index]
        self.board[index] = 0
        i = index
        while stones > 0:
            i = (i + 1) % 14
            if i == (7 if self.player_turn == 1 else 14):  # Skip opponent's store
                continue
            self.board[i] += 1
            stones -= 1

        # Capture rule
        if 0 <= i < 6 and self.player_turn == 0 and self.board[i] == 1 and self.board[12 - i] > 0:
            self.board[6] += self.board[i] + self.board[12 - i]
            self.board[i] = self.board[12 - i] = 0
        elif 7 <= i < 13 and self.player_turn == 1 and self.board[i] == 1 and self.board[12 - i] > 0:
            self.board[13] += self.board[i] + self.board[12 - i]
            self.board[i] = self.board[12 - i] = 0

        # Extra turn rule
        if i == 6 or i == 13:
            return  # Player gets another turn

        self.player_turn = 1 - self.player_turn  # Switch turns

    def evaluate(self):
        return self.board[6] - self.board[13]  # Score difference heuristic

    def minimax(self, depth, alpha, beta, maximizing_player):
        if depth == 0 or self.is_game_over():
            return self.evaluate(), None

        best_move = None
        if maximizing_player:
            max_eval = float('-inf')
            for move in self.get_valid_moves():
                new_board = copy.deepcopy(self)
                new_board.make_move(move)
                eval, _ = new_board.minimax(depth - 1, alpha, beta, False)
                if eval > max_eval:
                    max_eval = eval
                    best_move = move
                alpha = max(alpha, eval)
                if beta <= alpha:
                    break
            return max_eval, best_move
        else:
            min_eval = float('inf')
            for move in self.get_valid_moves():
                new_board = copy.deepcopy(self)
                new_board.make_move(move)
                eval, _ = new_board.minimax(depth - 1, alpha, beta, True)
                if eval < min_eval:
                    min_eval = eval
                    best_move = move
                beta = min(beta, eval)
                if beta <= alpha:
                    break
            return min_eval, best_move

    def log_game_state(self, best_move, filename="./data/mancala_games.csv"):
        """Appends the current board state and best move to a CSV file after every AI move."""
        file_exists = os.path.isfile(filename)
        with open(filename, mode="a", newline="") as file:
            writer = csv.writer(file)
            if not file_exists:
                header = [f"Pit_{i}" for i in range(14)] + ["Best_Move"]
                writer.writerow(header)
            writer.writerow(self.board + [best_move])

def time_tracker():
    numMinutesRun = 0
    while True:
        time.sleep(60)
        numMinutesRun += 1
        os.system('cls' if os.name == 'nt' else 'clear')
        if numMinutesRun > 60:
            hours = numMinutesRun // 60
            minutes = numMinutesRun % 60
            print(f"Time Elapsed: {hours} hours, {minutes} minutes")
            print(f"Games run: {numGamesRun}")
        else:
            print(f"Time Elapsed: {numMinutesRun} minutes")
            print(f"Games run: {numGamesRun}")

numGamesRun = 0

# Start time tracking thread
threading.Thread(target=time_tracker, daemon=True).start()

while True:
    game = Mancala()
    depth = 12  # Number of moves ahead the AI looks

    while not game.is_game_over():
        if not game.get_valid_moves():
            break  # End the game if no valid moves

        if game.player_turn == 0:  # AI Player
            _, best_move = game.minimax(depth, float('-inf'), float('inf'), True)
            if best_move is not None:
                game.log_game_state(best_move)
                game.make_move(best_move)
        else:
            # AI or human player logic for Player 2 (can be random moves for testing)
            move = random.choice(game.get_valid_moves())
            game.make_move(move)

    numGamesRun += 1
