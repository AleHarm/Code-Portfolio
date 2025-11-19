import torch
import pandas as pd
from torch.utils.data import Dataset, DataLoader
import numpy as np
from sklearn.model_selection import train_test_split

# 1. Dataset Class (0-5 labels)
class CSVDataset(Dataset):
    def __init__(self, csv_path):
        self.data = pd.read_csv(csv_path, header=None)
        self.X = self.data.iloc[:, :14].values.astype(np.float32)
        self.y = self.data.iloc[:, 14].values.astype(np.int64)  # 0-5 labels
        
        assert (self.y >= 0).all() and (self.y < 6).all(), "Labels must be 0-5"

    def __len__(self):
        return len(self.data)
    
    def __getitem__(self, idx):
        return torch.tensor(self.X[idx]), torch.tensor(self.y[idx])

# 2. MLP Model Definition
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
            torch.nn.Linear(64, 6)  # 6 classes (0-5)
        )
        
    def forward(self, x):
        return self.model(x)

# 3. Prediction with Fallback Logic
def predict_with_fallback(model, X, device):
    """Predict with fallback to next most likely when primary prediction is 0"""
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
    return predictions.cpu().numpy()  # 0-5 range

# 4. Training Function (Optional)
def train_model():
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    dataset = CSVDataset("your_data.csv")
    train_set, val_set = train_test_split(dataset, test_size=0.2, random_state=42)
    
    train_loader = DataLoader(train_set, batch_size=32, shuffle=True)
    val_loader = DataLoader(val_set, batch_size=32)
    
    model = BestMLP().to(device)
    criterion = torch.nn.CrossEntropyLoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=0.001)
    
    # ... (training loop from previous implementation)
    # torch.save(model.state_dict(), "best_model.pth")

# 5. Interactive Prediction Loop
def run_prediction_loop(model_path: str = "best_model.pth"):
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = BestMLP().to(device)
    model.load_state_dict(torch.load(model_path))
    model.eval()

    while True:
        try:
            user_input = input("\nEnter 14 comma-separated values (or 'q' to quit): ").strip()
            if user_input.lower() == 'q':
                print("Exiting...")
                break

            values = [float(x.strip()) for x in user_input.split(',')]
            if len(values) != 14:
                print("Error: Exactly 14 values required")
                continue

            input_tensor = torch.tensor(values, dtype=torch.float32).unsqueeze(0).to(device)
            prediction = predict_with_fallback(model, input_tensor, device)
            predicted_index = prediction[0]
            
            # Check if user's value at predicted index is 0
            while values[int(predicted_index)] == 0:
                print(f"Predicted {predicted_index} (user value is 0) - using fallback")
                # Get next prediction by masking previous prediction
                with torch.no_grad():
                    outputs = model(input_tensor)
                    probs = torch.nn.functional.softmax(outputs, dim=1)
                    probs[:, predicted_index] = -1  # Mask previous prediction
                    predicted_index = torch.argmax(probs).item()
            
            print(f"Final prediction: {predicted_index}")

        except ValueError as e:
            print(f"Invalid input: {str(e)}")
        except Exception as e:
            print(f"Error: {str(e)}")

# 6. Main Execution
if __name__ == "__main__":
    # Train or load model first (training code omitted)
    # train_model()
    
    # Start interactive loop
    run_prediction_loop()
