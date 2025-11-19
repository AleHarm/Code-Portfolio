import torch
import pandas as pd
from torch.utils.data import Dataset, DataLoader
import numpy as np
from sklearn.model_selection import train_test_split

# 1. Enhanced Dataset Class with normalization
class CSVDataset(Dataset):
    def __init__(self, csv_path):
        self.data = pd.read_csv(csv_path)
        self.X = self.data.iloc[:, :14].values.astype(np.float32)
        self.y = self.data.iloc[:, 14].values.astype(np.float32).reshape(-1, 1)
        
        # Store normalization parameters
        self.X_mean = self.X.mean(axis=0)
        self.X_std = self.X.std(axis=0)
        self.y_mean = self.y.mean()
        self.y_std = self.y.std()
        
        # Normalize data
        self.X = (self.X - self.X_mean) / (self.X_std + 1e-8)
        self.y = (self.y - self.y_mean) / (self.y_std + 1e-8)
        
    def __len__(self):
        return len(self.data)
    
    def __getitem__(self, idx):
        return torch.tensor(self.X[idx]), torch.tensor(self.y[idx])
    
    def unnormalize(self, tensor):
        """Convert normalized tensor back to original scale"""
        return tensor * self.y_std + self.y_mean

# 2. Optimized Model Architecture
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
            torch.nn.Linear(64, 1)
        )
        
    def forward(self, x):
        return self.model(x)

# 3. Training Setup and Execution with CUDA Support
def train_and_evaluate(csv_path, save_name="best_model.pth"):
    # Check if CUDA is available
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print(f"Using device: {device}")
    
    # Initialize dataset and split
    dataset = CSVDataset(csv_path)
    train_set, val_set = train_test_split(dataset, test_size=0.2, random_state=42)
    
    # Create data loaders
    train_loader = DataLoader(train_set, batch_size=32, shuffle=True)
    val_loader = DataLoader(val_set, batch_size=32)
    
    # Model setup
    model = BestMLP().to(device)  # Move model to GPU if available
    criterion = torch.nn.MSELoss().to(device)  # Move loss function to GPU
    optimizer = torch.optim.Adam(model.parameters(), lr=0.001)
    best_val_loss = float('inf')
    
    # Training loop
    for epoch in range(100):
        model.train()
        train_loss = 0
        for X_batch, y_batch in train_loader:
            X_batch, y_batch = X_batch.to(device), y_batch.to(device)  # Move data to GPU
            
            optimizer.zero_grad()
            outputs = model(X_batch)
            loss = criterion(outputs, y_batch)
            loss.backward()
            optimizer.step()
            train_loss += loss.item()
            
        # Validation
        model.eval()
        val_loss = 0
        with torch.no_grad():
            for X_val, y_val in val_loader:
                X_val, y_val = X_val.to(device), y_val.to(device)  # Move data to GPU
                
                outputs = model(X_val)
                val_loss += criterion(outputs, y_val).item()
                
        avg_train_loss = train_loss / len(train_loader)
        avg_val_loss = val_loss / len(val_loader)
        
        # Save best model
        if avg_val_loss < best_val_loss:
            best_val_loss = avg_val_loss
            torch.save(model.state_dict(), save_name)
            
        print(f"Epoch {epoch+1}/100 | Train Loss: {avg_train_loss:.4f} | Val Loss: {avg_val_loss:.4f}")
    
    print(f"\nTraining complete. Best validation loss: {best_val_loss:.4f}")
    return model

# 4. Inference Example with CUDA Support
def predict(model, dataset, input_data):
    """Predict using raw input data (unnormalized)"""
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    
    # Normalize input and move to GPU if available
    input_norm = (input_data - dataset.X_mean) / (dataset.X_std + 1e-8)
    input_tensor = torch.tensor(input_norm).float().to(device)
    
    with torch.no_grad():
        prediction = model(input_tensor).cpu()  # Move prediction back to CPU for unnormalization
    
    return dataset.unnormalize(prediction).item()

# Usage Example
if __name__ == "__main__":
    # 1. Train the model
    csv_path = 'E:\CS 474 Final Project\Data Generation\data\MiniMax_Random_D12_G1000.csv'  # Replace with your CSV path
    model = train_and_evaluate(csv_path)
    
    # Load trained model for inference (optional step after training is complete)
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    print(device)
    model.load_state_dict(torch.load("best_model.pth"))
    model.to(device)  # Ensure the loaded model is on the correct device
    
    # 2. Example prediction
    dataset = CSVDataset(csv_path)
    example_input = np.random.rand(14)  # Replace with actual input data (14 features)
    prediction = predict(model, dataset, example_input)
    
    print(f"\nPredicted output: {prediction:.2f}")
