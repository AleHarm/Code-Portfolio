import torch
import pandas as pd
from torch.utils.data import Dataset, DataLoader
import numpy as np
from sklearn.model_selection import train_test_split

# 1. Dataset Class
class CSVDataset(Dataset):
    def __init__(self, csv_path):
        self.data = pd.read_csv(csv_path)
        self.X = self.data.iloc[:, :14].values.astype(np.float32)
        self.y = self.data.iloc[:, 14].values.astype(np.float32).reshape(-1, 1)
        
    def __len__(self):
        return len(self.data)
    
    def __getitem__(self, idx):
        return torch.tensor(self.X[idx]), torch.tensor(self.y[idx])

# 2. MLP Model Definition
class MLP(torch.nn.Module):
    def __init__(self, input_size=14, hidden_sizes=[64], output_size=1, 
                 activation='relu', dropout=0.2):
        super().__init__()
        layers = []
        prev_size = input_size
        
        for size in hidden_sizes:
            layers.append(torch.nn.Linear(prev_size, size))
            if activation == 'relu':
                layers.append(torch.nn.ReLU())
            elif activation == 'leakyrelu':
                layers.append(torch.nn.LeakyReLU())
            elif activation == 'tanh':
                layers.append(torch.nn.Tanh())
            layers.append(torch.nn.Dropout(dropout))
            prev_size = size
            
        layers.append(torch.nn.Linear(prev_size, output_size))
        self.model = torch.nn.Sequential(*layers)
        
    def forward(self, x):
        return self.model(x)

# 3. Hyperparameter Setup
hyperparameter_grid = {
    'hidden_layers': [
        [64], 
        [128, 64],
        [256, 128, 64],
        [512, 256, 128]
    ],
    'learning_rate': [1e-2, 1e-3, 1e-4],
    'batch_size': [16, 32, 64],
    'dropout': [0.0, 0.2, 0.4],
    'activation': ['relu', 'leakyrelu', 'tanh'],
    'epochs': [50, 100]
}

# 4. Training/Validation Function
def train_model(params, dataset):
    train_set, val_set = train_test_split(dataset, test_size=0.2)
    
    train_loader = DataLoader(train_set, batch_size=params['batch_size'], shuffle=True)
    val_loader = DataLoader(val_set, batch_size=params['batch_size'])
    
    model = MLP(
        hidden_sizes=params['hidden_layers'],
        activation=params['activation'],
        dropout=params['dropout']
    )
    criterion = torch.nn.MSELoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=params['learning_rate'])
    
    best_val_loss = float('inf')
    for epoch in range(params['epochs']):
        # Training
        model.train()
        for X_batch, y_batch in train_loader:
            optimizer.zero_grad()
            outputs = model(X_batch)
            loss = criterion(outputs, y_batch)
            loss.backward()
            optimizer.step()
        
        # Validation
        model.eval()
        val_loss = 0
        with torch.no_grad():
            for X_val, y_val in val_loader:
                outputs = model(X_val)
                val_loss += criterion(outputs, y_val).item()
        avg_val_loss = val_loss / len(val_loader)
        
        if avg_val_loss < best_val_loss:
            best_val_loss = avg_val_loss
            
    return best_val_loss

# 5. Hyperparameter Search
if __name__ == "__main__":
    dataset = CSVDataset('E:\CS 474 Final Project\Data Generation\data\MiniMax_Random_D12_G1000.csv')  # Replace with CSV path
    best_loss = float('inf')
    best_params = {}
    
    # Grid Search
    from itertools import product
    for params in product(*hyperparameter_grid.values()):
        param_dict = dict(zip(hyperparameter_grid.keys(), params))
        try:
            loss = train_model(param_dict, dataset)
            if loss < best_loss:
                best_loss = loss
                best_params = param_dict
                print(f"New best: Loss={loss:.4f}, Params={param_dict}")
        except Exception as e:
            print(f"Failed with {param_dict}: {str(e)}")
    
    print(f"\nBest Parameters: {best_params}")
    print(f"Best Validation Loss: {best_loss:.4f}")
