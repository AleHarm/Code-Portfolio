# mancala_game.py

import torch
from model3 import BestMLP, predict_with_fallback

def initial_board():
    return [4] * 6 + [0] + [4] * 6 + [0]

def move(board, pit, player):
    side_offset = 0 if player == 0 else 7
    if board[side_offset + pit] == 0:
        return board, False, False

    stones = board[side_offset + pit]
    board[side_offset + pit] = 0
    idx = side_offset + pit

    while stones:
        idx = (idx + 1) % 14
        if player == 0 and idx == 13: continue
        if player == 1 and idx == 6: continue
        board[idx] += 1
        stones -= 1

    extra_turn = (player == 0 and idx == 6) or (player == 1 and idx == 13)
    
    if player == 0 and 0 <= idx < 6 and board[idx] == 1 and board[12 - idx] > 0:
        board[6] += board[idx] + board[12 - idx]
        board[idx] = board[12 - idx] = 0
    elif player == 1 and 7 <= idx < 13 and board[idx] == 1 and board[12 - idx] > 0:
        board[13] += board[idx] + board[12 - idx]
        board[idx] = board[12 - idx] = 0

    return board, extra_turn, True

def get_bot_move(board, model, device):
    input_board = board[:6] + [board[6]] + board[7:13] + [board[13]]
    input_tensor = torch.tensor(input_board, dtype=torch.float32).unsqueeze(0).to(device)
    pred = predict_with_fallback(model, input_tensor, device)[0]

    valid_moves = [i for i in range(6) if board[7 + i] > 0]
    if pred in valid_moves:
        return pred
    for i in valid_moves:
        test_board, _, _ = move(board.copy(), i, 1)
        if test_board[13] > board[13]:
            return i
    return max(valid_moves, default=0)

def get_human_move(board):
    # Strategy: Extra turn > Capture > Farthest right
    for i in range(6):
        temp_board, extra, _ = move(board.copy(), i, 0)
        if extra: return i
    for i in range(6):
        if board[i] == 0 and board[12 - i] > 0:
            return i
    for i in reversed(range(6)):
        if board[i] > 0:
            return i
    return 0

def is_game_over(board):
    return sum(board[:6]) == 0 or sum(board[7:13]) == 0

def finalize_game(board):
    board[6] += sum(board[:6])
    board[13] += sum(board[7:13])
    for i in range(6): board[i] = 0
    for i in range(7, 13): board[i] = 0
    return board

def play_game(model_path="best_model.pth"):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = BestMLP().to(device)
    model.load_state_dict(torch.load(model_path))
    model.eval()

    board = initial_board()
    player = 0  # Human goes first

    while not is_game_over(board):
        if player == 0:
            move_idx = get_human_move(board)
            board, extra_turn, valid = move(board, move_idx, 0)
        else:
            move_idx = get_bot_move(board, model, device)
            board, extra_turn, valid = move(board, move_idx, 1)
        
        if not extra_turn:
            player = 1 - player

    board = finalize_game(board)
    print("Final board:", board)
    print("Human store:", board[6])
    print("Bot store:", board[13])
    winner = "Human" if board[6] > board[13] else "Bot" if board[13] > board[6] else "Tie"
    print("Winner:", winner)

if __name__ == "__main__":
    play_game()
