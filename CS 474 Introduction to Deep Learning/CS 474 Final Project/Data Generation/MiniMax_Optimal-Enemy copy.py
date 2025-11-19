import copy
import csv
import os
import threading
import time

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

    def decide_optimal_enemy_move(self):
        # Extra turn
        for move in self.get_valid_moves():
            temp_game = copy.deepcopy(self)
            temp_game.make_move(move)
            if temp_game.player_turn == self.player_turn:
                return move

        # Get a hole capture
        holeCaptures = {}
        for move in self.get_valid_moves():
            numPieces = self.board[move]
            newIndex = move
            for i in range(numPieces):
                newIndex += 1
                newIndex = newIndex % 14
                if newIndex == 6:
                    newIndex += 1
            if 6 < newIndex < 13 and self.board[newIndex] == 0:
                holeCaptures[move] = 1 + self.board[12 - move]
        if holeCaptures:
            max_key = max(holeCaptures, key=holeCaptures.get)
            return max_key

        # Closest hole to the store
        for move in range(12, 6, -1):
            if self.board[move] != 0:
                return move - 7

    def log_game_state(self, best_move, filename):
        """Appends the current board state and best move to a CSV file after every AI move."""
        file_exists = os.path.isfile(filename)
        with open(filename, mode="a", newline="") as file:
            writer = csv.writer(file)
            if not file_exists:
                header = [f"Pit_{i}" for i in range(14)] + ["Best_Move"]
                writer.writerow(header)
            writer.writerow(self.board + [best_move])

    def log_game_time(self, time, filename):
        file_exists = os.path.isfile(filename)
        with open(filename, mode="a", newline="") as file:
            writer = csv.writer(file)
            writer.writerow([f"\nTime: {time // 60}hrs {time % 60} min"])


def time_tracker():
    global numMinutesRun
    while True:
        time.sleep(60)
        numMinutesRun += 1
        os.system('cls' if os.name == 'nt' else 'clear')
        if numMinutesRun > 60:
            hours = numMinutesRun // 60
            minutes = numMinutesRun % 60
            print(f"Time Elapsed: {hours} hours, {minutes} minutes")
        else:
            print(f"Time Elapsed: {numMinutesRun} minutes")


# Global variables
numGamesRun = 0
numMinutesRun = 0

# Start time tracking thread
threading.Thread(target=time_tracker, daemon=True).start()

baseNum = 19
depth = baseNum

while True:
    depth = baseNum + numGamesRun  # Number of moves ahead the AI looks
    game = Mancala()
    filename = f"./data/MiniMax_Optimal_D{depth}.csv"

    while not game.is_game_over():
        if not game.get_valid_moves():
            break  # End the game if no valid moves

        if game.player_turn == 0:  # AI Player
            _, best_move = game.minimax(depth, float('-inf'), float('inf'), True)
            if best_move is not None:
                game.log_game_state(best_move, filename)
                game.make_move(best_move)
        else:
            # AI-controlled opponent move selection
            move = game.decide_optimal_enemy_move()
            if move is not None:
                game.make_move(move)

    numGamesRun += 1
    game.log_game_time(numMinutesRun, filename)
    numMinutesRun = 0  # Reset timer after each game
