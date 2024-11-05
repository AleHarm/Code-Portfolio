using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChaosHoles : MonoBehaviour
{
    const float DELAY_TIME = .2f;
    const int PLAYER_1_POINT_HOLE_INDEX = 6;
    const int PLAYER_2_POINT_HOLE_INDEX = 13;
    const int TOTAL_PIECE_DANCE_ANIMATIONS = 54;

    public GameObject gamePiece;
    public Material highlightPass1;
    public Material highlightPass2;
    public Material opponentHighlight;
    public Material badFlashHighlight;
    public Material goodFlashHighlight;
    public Material invisible;
    public bool isPlayer1Turn;

    GameObject lastTriggerMousedOver;
    GameObject HUD;
    List<Transform> highlightedHoles;
    string gamePieceName;
    private bool isInMenu = false;



    void OnEnable(){

        gamePieceName = gamePiece.name;
        int layerMask = 1 << LayerMask.NameToLayer("IgnoreMouseOver");
        HUD = GameObject.Find("HUD");
        StartCoroutine(EnableStartingPieces());
    }

    void Update(){

        int layerMask = 1 << LayerMask.NameToLayer("IgnoreMouseOver");
        Ray ray = Camera.main.ScreenPointToRay(Input.mousePosition);
        RaycastHit hit;
        if (Physics.Raycast(ray, out hit, Mathf.Infinity, ~layerMask) 
                && hit.collider.gameObject.name == "MouseOverTrigger"
                && !isInMenu){
            GameObject gameObjectHit = hit.collider.gameObject;
            if(gameObjectHit != lastTriggerMousedOver){
                lastTriggerMousedOver = gameObjectHit;
                HighlightHoles(gameObjectHit);
            }
            if(Input.GetMouseButtonDown(0)){
                StartCoroutine(MovePieces(gameObjectHit.transform.parent.transform));        
            }
        }else{
            lastTriggerMousedOver = null;
            RemoveHighlights();
        }
    }

    public void SetIsInMenu(bool value){

        isInMenu = value;
    }

    void HighlightHoles(GameObject gameObjectHit){

        GameObject hole = gameObjectHit.transform.parent.gameObject;
        int totalHoles = this.transform.childCount;
        int numHolesToHighlight = hole.GetComponent<Hole>().numPieces;
        bool anotherRound = numHolesToHighlight >= totalHoles;
        string holeName = hole.name;
        int childIndex = FindChildIndexByName(this.transform, holeName);
        int index = childIndex + 1;
        int endHoleIndex = ((childIndex + 1) + numHolesToHighlight) % this.transform.childCount;
        Material highlight = highlightPass1;
        List<Transform> highlightedHoles = new List<Transform>();
        while(true){
            index = index % totalHoles;
            if(index == endHoleIndex && !anotherRound){
                break;
            }
            if(index == childIndex && anotherRound){
                anotherRound = false;
            }
            if(index == childIndex + 1 && numHolesToHighlight >= 14 && !anotherRound){
                highlight = highlightPass2;
            }
            Transform child = this.transform.GetChild(index);
            child.GetChild(FindChildIndexByName(child, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = highlight;
            highlightedHoles.Add(child);
            index++;
        }
        this.highlightedHoles = highlightedHoles;
    }

    void RemoveHighlights(){

      for(int i = 0; i < this.transform.childCount; i++){
        Transform child = this.transform.GetChild(i);
        child.GetChild(FindChildIndexByName(child, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = invisible;         
      }
    }

    int FindChildIndexByName(Transform parent, string childName){
        for (int i = 0; i < parent.childCount; i++){
            if (parent.GetChild(i).name == childName){
                return i;
            }
        }
        return -1;
    }

    IEnumerator MovePieces(Transform hole){

        List<Transform> piecesBeingMoved = new List<Transform>();

        foreach (Transform child in hole){
            if(child.gameObject.name != "MouseOverTrigger"){
                child.gameObject.SetActive(false);
                piecesBeingMoved.Add(child);
            }
        }

        RemoveHighlights();

        for(int i = 0; i < this.highlightedHoles.Count; i++){
            Transform currentPiece = piecesBeingMoved[i];
            Transform currentHole = highlightedHoles[i];

            currentPiece.position = new Vector3(currentHole.position.x, currentPiece.position.y + 5, currentHole.position.z - .5f);
            currentPiece.SetParent(currentHole);
            currentPiece.gameObject.SetActive(true);
            yield return new WaitForSecondsRealtime(DELAY_TIME);
        }
    }  

    public IEnumerator EnableStartingPieces(){

        yield return new WaitForSecondsRealtime(3f);

        for (int i = 0; i < 6; i++){
            EnableAllPiecesInHole(transform.GetChild(i));
            EnableAllPiecesInHole(transform.GetChild(12 - i));
            yield return new WaitForSecondsRealtime(DELAY_TIME);
        }
   }

    void EnableAllPiecesInHole(Transform hole){

        foreach (Transform childTransform in hole.transform){
            GameObject childGameObject = childTransform.gameObject;
            if (childGameObject.name == gamePieceName){
                childGameObject.SetActive(true);
            }
        }
    }
}