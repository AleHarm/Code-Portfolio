# mlp_model.py

import torch
import pandas as pd
from torch.utils.data import Dataset, DataLoader
import numpy as np
from sklearn.model_selection import train_test_split

class CSVDataset(torch.utils.data.Dataset):
    def __init__(self, csv_path):
        self.data = pd.read_csv(csv_path, header=None)
        self.X = self.data.iloc[:, :14].values.astype(np.float32)
        self.y = self.data.iloc[:, 14].values.astype(np.int64)
        assert (self.y >= 0).all() and (self.y < 6).all(), "Labels must be 0-5"

    def __len__(self):
        return len(self.data)
    
    def __getitem__(self, idx):
        return torch.tensor(self.X[idx]), torch.tensor(self.y[idx])

class BestMLP(torch.nn.Module):
    def __init__(self):
        super().__init__()
        self.model = torch.nn.Sequential(
            torch.nn.Linear(14, 256),
            torch.nn.Tanh(),
            torch.nn.Dropout(0.2),
            torch.nn.Linear(256, 128),
            torch.nn.Tanh(),
            torch.nn.Dropout(0.2),
            torch.nn.Linear(128, 64),
            torch.nn.Tanh(),
            torch.nn.Dropout(0.2),
            torch.nn.Linear(64, 6)
        )
        
    def forward(self, x):
        return self.model(x)

def predict_with_fallback(model, X, device):
    with torch.no_grad():
        X = X.to(device)
        outputs = model(X)
        probs = torch.nn.functional.softmax(outputs, dim=1)
        top2 = torch.topk(probs, k=2, dim=1)
        predictions = torch.where(
            top2.indices[:, 0] == 0,
            top2.indices[:, 1],
            top2.indices[:, 0]
        )
    return predictions.cpu().numpy()
