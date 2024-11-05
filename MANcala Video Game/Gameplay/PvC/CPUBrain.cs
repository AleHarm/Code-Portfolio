using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CPUBrain : MonoBehaviour
{

    public GameObject HUDPvC;
    public Transform holesPvC;
    
    private bool makingMove = false;
    public bool isEasy; //TODO: change to private

    void Start(){

        isEasy = PlayerPrefs.GetInt("isEasy") == 1;
    }

    void Update(){

        if(holesPvC == null && GameObject.FindWithTag("Holes") != null){
            holesPvC = GameObject.FindWithTag("Holes").transform;
        }
        if(!HUDPvC.GetComponent<HUDPvC>().isPlayerTurn && !makingMove){
            Transform hole = DecideMove();
            if(hole != null){
                StartCoroutine(MakeMove(hole));
            }
        }
    }

    Transform DecideMove(){

        makingMove = true;

        if(isEasy){
            return Easy();
        }else{
            return Normal();
        }
    }

    IEnumerator MakeMove(Transform hole){

        yield return new WaitForSecondsRealtime(1f);

        holesPvC.GetComponent<HolesPvC>().isPlayerTurn = false;
        holesPvC.GetComponent<HolesPvC>().HighlightHoles(hole.GetChild(FindChildIndexByName(hole, "MouseOverTrigger")).gameObject);
        yield return StartCoroutine(holesPvC.GetComponent<HolesPvC>().MovePieces(hole));
        holesPvC.GetComponent<HolesPvC>().isPlayerTurn = true;
        makingMove = false;
    }

    Transform Easy(){

        while(true){
            int holeIndexToMove = Random.Range(7, 13);
            if(holesPvC.GetChild(holeIndexToMove).childCount > 1){
                return holesPvC.GetChild(holeIndexToMove);
            }
        }
    }

    Transform Normal(){

        Transform holeToReturn = null;
        List<Transform> emptyHoles = new List<Transform>{};
        
        holeToReturn = GetHoleThatGivesExtraTurn(emptyHoles);

        if(holeToReturn != null){
            return holeToReturn;
        }

        Dictionary<Transform, int> pointsScoredOnEmptyHole = new Dictionary<Transform, int>();

        PopulateReachableEmptyHolesDictionary(emptyHoles, pointsScoredOnEmptyHole);
        holeToReturn = GetBestReachableEmptyHole(pointsScoredOnEmptyHole);

        if(holeToReturn != null){
            return holeToReturn;
        }

        holeToReturn = GetFirstNonEmptyHoleByPointHole();

        if(holeToReturn != null){
            return holeToReturn;
        }else{
            Debug.LogError("Tried to move when there are no moves");
            return null;
        }
    }

    Transform GetHoleThatGivesExtraTurn(List<Transform> emptyHoles){

        for(int i = 7; i < holesPvC.childCount - 1; i++){
            Transform currentHole = holesPvC.GetChild(i);
            int numPiecesInHole = currentHole.childCount - 1;

            if(((i + numPiecesInHole) % 13) == 0){
                return currentHole;
            }
            if(numPiecesInHole == 0){
                AddHoleToEmptyHoles(currentHole, emptyHoles);
            }
        }

        return null;
    }

    void AddHoleToEmptyHoles(Transform hole, List<Transform> emptyHoles){

        emptyHoles.Add(hole);
    }

    void PopulateReachableEmptyHolesDictionary(List<Transform> emptyHoles, Dictionary<Transform, int> pointsScoredOnEmptyHole){

        foreach(Transform emptyHole in emptyHoles){

            int emptyHoleIndex = emptyHole.GetSiblingIndex();

            for(int i = 7; i < holesPvC.childCount - 1; i++){ 

                Transform currentHole = holesPvC.GetChild(i);
                int numPiecesInHole = currentHole.childCount - 1;

                if(i != emptyHoleIndex && ((i + numPiecesInHole) % 13) == emptyHoleIndex){

                    int numPointsCanGet = holesPvC.GetChild(12 - emptyHoleIndex).childCount - 1;
                    pointsScoredOnEmptyHole.Add(currentHole, numPointsCanGet);
                }
            }
        }
    }

    Transform GetBestReachableEmptyHole(Dictionary<Transform, int> pointsScoredOnEmptyHole){

        if(pointsScoredOnEmptyHole.Count > 0){

            Transform bestEmptyHole = null;

            foreach (KeyValuePair<Transform, int> kvp in pointsScoredOnEmptyHole){
                Transform hole = kvp.Key;
                int pointsCanScore = kvp.Value;

                if(bestEmptyHole == null || pointsCanScore > pointsScoredOnEmptyHole[bestEmptyHole]){
                    bestEmptyHole = hole;
                }
            }

            return bestEmptyHole;
        }
        
        return null;
    }

    Transform GetFirstNonEmptyHoleByPointHole(){

        for(int i = 12; i > 6; i--){

            Transform currentHole = holesPvC.GetChild(i);
            if(currentHole.childCount > 1){
                return currentHole;
            }
        }

        return null;
    }

    Transform Hard(){

        return null;
    }

    int FindChildIndexByName(Transform parent, string childName){
        for (int i = 0; i < parent.childCount; i++){
            if (parent.GetChild(i).name == childName){
                return i;
            }
        }
        return -1;
    }
}
