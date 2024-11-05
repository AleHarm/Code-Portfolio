using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Holes : MonoBehaviour
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
    bool isMovingPieces = false;
    bool isFlashingHighlight = false;
    bool isGameOver = false;
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
                && !isMovingPieces
                && !isFlashingHighlight
                && !isGameOver
                && !isInMenu){
            GameObject gameObjectHit = hit.collider.gameObject;
            isPlayer1Turn = HUD.GetComponent<HUD>().isPlayer1Turn;
            if(gameObjectHit != lastTriggerMousedOver){
                lastTriggerMousedOver = gameObjectHit;
                RemoveHighlights();
                HighlightHoles(gameObjectHit);
            }
            if(Input.GetMouseButtonDown(0)){
                if(IsPlayerAllowedToMove(gameObjectHit)){
                    StartCoroutine(MovePieces(gameObjectHit.transform.parent.transform));  
                }else{
                    StartCoroutine(FlashOpponentHighlight());
                }        
            }
        }else{
            lastTriggerMousedOver = null;
            if(!isFlashingHighlight){

                RemoveHighlights();
            }
        }
    }

    public void SetIsInMenu(bool value){

        isInMenu = value;
    }

    void HighlightHoles(GameObject gameObjectHit){

        GameObject hole = gameObjectHit.transform.parent.gameObject;
        int numHolesToHighlight = hole.GetComponent<Hole>().numPieces;
        bool anotherRound = numHolesToHighlight >= 14;
        string holeName = hole.name;
        int childIndex = FindChildIndexByName(this.transform, holeName);
        int index = childIndex + 1;
        int endHoleIndex = ((childIndex + 1) + numHolesToHighlight) % this.transform.childCount;
        bool isPlayer1Side = (childIndex < 7);
        Material highlight = highlightPass1;
        List<Transform> highlightedHoles = new List<Transform>();

        while(true){
            if((isPlayer1Turn && isPlayer1Side && index == PLAYER_2_POINT_HOLE_INDEX) 
                    || (!isPlayer1Turn && isPlayer1Side && index == PLAYER_2_POINT_HOLE_INDEX)
                    || (isPlayer1Turn && !isPlayer1Side && index == PLAYER_1_POINT_HOLE_INDEX)
                    || (!isPlayer1Turn && !isPlayer1Side && index == PLAYER_1_POINT_HOLE_INDEX)){
                index++;
                endHoleIndex++;
                endHoleIndex = endHoleIndex % this.transform.childCount;
            }
            index = index % this.transform.childCount;
            if(index == endHoleIndex && !anotherRound){
                break;
            }
            if(index == childIndex && anotherRound){
                anotherRound = false;
            }
            if(isPlayer1Side != isPlayer1Turn){
                highlight = opponentHighlight;
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

        isMovingPieces = true;
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

        bool didLandInEmptyHole = DidLandInEmptyHole();
        if(didLandInEmptyHole){
            yield return StartCoroutine(MoveBonusPiecesToPointHole());
        }

        bool extraTurn = DidLandInPointHole();
        bool currentPlayerHasMoves = DoesPlayerHaveMoves(isPlayer1Turn);
        bool opponentHasMoves = DoesPlayerHaveMoves(!isPlayer1Turn);

        if(!opponentHasMoves && !currentPlayerHasMoves){
            //Game Over
            int p1Score = this.transform.Find("Player 1 Point Hole").childCount - 1;
            int p2Score = this.transform.Find("Player 2 Point Hole").childCount - 1;
            int didPlayer1Win = DetermineWinner(p1Score, p2Score);
            StartCoroutine(EndGame(didPlayer1Win));
            yield break;
        }else if(!opponentHasMoves){
            extraTurn = true;
        }else if(!currentPlayerHasMoves){
            extraTurn = false;
        }
        yield return new WaitForSecondsRealtime(1f);
        if(extraTurn){
            HUD.GetComponent<HUD>().ManageTurns(isPlayer1Turn);
        }else{
            HUD.GetComponent<HUD>().ManageTurns(!isPlayer1Turn);
        }

        isMovingPieces = false;
    }

    bool DidLandInPointHole(){

        Transform lastHole = highlightedHoles[highlightedHoles.Count - 1];

        if(isPlayer1Turn && lastHole.name == "Player 1 Point Hole"){
            StartCoroutine(FlashHoleGreen(lastHole));
            return true;
        }else if(!isPlayer1Turn && lastHole.name == "Player 2 Point Hole"){
            StartCoroutine(FlashHoleGreen(lastHole));
            return true;
        }else{
            return false;
        }
    }   

    public IEnumerator EnableStartingPieces(){

        isMovingPieces = true;

        for (int i = 0; i < 6; i++){
            EnableAllPiecesInHole(transform.GetChild(i));
            EnableAllPiecesInHole(transform.GetChild(12 - i));
            yield return new WaitForSecondsRealtime(DELAY_TIME);
        }
        
        isMovingPieces = false;
    }

    void EnableAllPiecesInHole(Transform hole){

        foreach (Transform childTransform in hole.transform){
            GameObject childGameObject = childTransform.gameObject;
            if (childGameObject.name == gamePieceName){
                childGameObject.SetActive(true);
            }
        }
    }

    bool IsPlayerAllowedToMove(GameObject mouseOverTrigger){

        GameObject hole = mouseOverTrigger.transform.parent.gameObject;
        int holeIndex = FindChildIndexByName(this.transform, hole.name);

        if(hole.transform.childCount == 1){
            return false;
        }

        if(holeIndex < 6 && isPlayer1Turn){
            return true;
        }else if(holeIndex > 6 && !isPlayer1Turn){
            return true;
        }else{
            return false;
        }
    }

    IEnumerator FlashOpponentHighlight(){

        for(int i = 0; i < this.highlightedHoles.Count; i++){
            Transform currentHole = highlightedHoles[i];
            currentHole.GetChild(FindChildIndexByName(currentHole, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = badFlashHighlight;         
        }
        yield return new WaitForSecondsRealtime(DELAY_TIME);
        for(int i = 0; i < this.highlightedHoles.Count; i++){
            Transform currentHole = highlightedHoles[i];
            currentHole.GetChild(FindChildIndexByName(currentHole, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = opponentHighlight;         
        }
    }

    bool DidLandInEmptyHole(){

        Transform lastHole = highlightedHoles[highlightedHoles.Count - 1];
        int lastHoleIndex = FindChildIndexByName(this.transform, lastHole.name);
        bool isPlayer1Side = (lastHoleIndex < 6);

        if(isPlayer1Side != isPlayer1Turn){
            return false;
        }
        if(lastHoleIndex != 6 && lastHoleIndex != 13){
            int numChildrenInHole = lastHole.childCount - 1; //Subtract 1 because of mouseOverTrigger
            if(numChildrenInHole == 1){
                return true;
            }
        }

        return false;
    }

    IEnumerator MoveBonusPiecesToPointHole(){

        Transform lastHole = highlightedHoles[highlightedHoles.Count - 1];
        int lastHoleIndex = FindChildIndexByName(this.transform, lastHole.name);
        int oppositeHoleIndex = 12 - lastHoleIndex;
        Transform oppositeHole = this.transform.GetChild(oppositeHoleIndex);
        List<Transform> piecesBeingMoved = new List<Transform>();
        Transform pointHole;

        yield return StartCoroutine(FlashHoleGreen(lastHole));
        if(isPlayer1Turn){
            pointHole = this.transform.GetChild(PLAYER_1_POINT_HOLE_INDEX);
        }else{
            pointHole = this.transform.GetChild(PLAYER_2_POINT_HOLE_INDEX);
        }
        foreach (Transform child in lastHole){
            if(child.gameObject.name != "MouseOverTrigger"){
                child.gameObject.SetActive(false);
                piecesBeingMoved.Add(child);
            }
        }
        foreach (Transform child in oppositeHole){
            if(child.gameObject.name != "MouseOverTrigger"){
                child.gameObject.SetActive(false);
                piecesBeingMoved.Add(child);
            }
        }
        for(int i = 0; i < piecesBeingMoved.Count; i++){
            Transform currentPiece = piecesBeingMoved[i];
            float zOffset = Random.Range(-1.3f, 1.3f);
            currentPiece.position = new Vector3(pointHole.position.x, currentPiece.position.y + 5, pointHole.position.z + zOffset - .5f);
            currentPiece.SetParent(pointHole);
            currentPiece.gameObject.SetActive(true);
            yield return new WaitForSecondsRealtime(DELAY_TIME);
        }
    }

    IEnumerator FlashHoleGreen(Transform hole){
        isFlashingHighlight = true;
        //Wait for piece to fall
        yield return new WaitForSecondsRealtime(1.2f);
        AudioManager.Instance.PlaySFX("Point");
        for(int i = 0; i < 2; i++){
            hole.GetChild(FindChildIndexByName(hole, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = goodFlashHighlight;         
            yield return new WaitForSecondsRealtime(.15f);
            hole.GetChild(FindChildIndexByName(hole, "MouseOverTrigger")).gameObject.GetComponent<MeshRenderer>().material = invisible;         
            yield return new WaitForSecondsRealtime(.15f);
        }

        isFlashingHighlight = false;
    }

    bool DoesPlayerHaveMoves(bool isPlayer1) {
        int startIndex;
        int endIndex;

        if (isPlayer1) {
            startIndex = 0;
            endIndex = 6;
        } else {
            startIndex = 7;
            endIndex = 13;
        }
        for (int i = startIndex; i < endIndex; i++) {
            Transform currentHole = this.transform.GetChild(i);
            if (currentHole.childCount > 1) {
                return true;
            }
        }
        return false;
    }

    IEnumerator EndGame(int didPlayer1Win){
        isGameOver = true;
        yield return new WaitForSecondsRealtime(1.2f);
        HUD.GetComponent<HUD>().FadeInWinUI(didPlayer1Win);
        BeginEndPieceAnimations();

    }

    void BeginEndPieceAnimations(){

        Transform player1PointHole = this.transform.Find("Player 1 Point Hole");
        foreach (Transform pieceTransform in player1PointHole){
            GameObject pieceGameObject = pieceTransform.gameObject;
            if (pieceGameObject.name == gamePieceName){
                StartCoroutine(AnimatePiece(pieceGameObject));
            }
        }
        Transform player2PointHole = this.transform.Find("Player 2 Point Hole");
        foreach (Transform pieceTransform in player2PointHole){
            GameObject pieceGameObject = pieceTransform.gameObject;
            if (pieceGameObject.name == gamePieceName){
                StartCoroutine(AnimatePiece(pieceGameObject));
            }
        }
    }

    IEnumerator AnimatePiece(GameObject piece){
        Vector3 pelvisPosition = piece.transform.Find("root").Find("pelvis").position;
        piece.transform.position = pelvisPosition;
        Animator animator = piece.GetComponent<Animator>();
        animator.enabled = true;

        yield return StartCoroutine(RotatePieceToUpright(piece));
        StartDance(animator);
        StartCoroutine(MovePieceUpAndAway(piece));
    }

    void StartDance(Animator animator){

        int animationNumber = Random.Range(1, TOTAL_PIECE_DANCE_ANIMATIONS + 1);
        animator.SetInteger("Dance", animationNumber);
    }

    IEnumerator RotatePieceToUpright(GameObject piece){
        float elapsedTime = 0f;
        float rotationDuration = 1f;
        Quaternion initialRotation = piece.transform.rotation;
        Quaternion targetRotation = Quaternion.Euler(0f, Random.Range(0f, 360f), 0f);

        while (elapsedTime < rotationDuration){
            piece.transform.rotation = Quaternion.Slerp(initialRotation, targetRotation, elapsedTime / rotationDuration);
            elapsedTime += Time.deltaTime;
            //Wait one frame
            yield return null;
        }
        piece.transform.rotation = targetRotation;
    }

    IEnumerator MovePieceUpAndAway(GameObject piece){

        float elapsedTime = 0f;
        float animationDuration = 60f;
        Vector3 initialPosition = piece.transform.position;
        Vector3 localUpDirection = piece.transform.TransformDirection(Vector3.up);
        Vector3 localForwardDirection = piece.transform.TransformDirection(Vector3.forward);
        float outwardSpeed = Random.Range(20f, 70f);
        Vector3 targetPosition = initialPosition + localUpDirection * 5f + localForwardDirection * outwardSpeed;

        while (elapsedTime < animationDuration){
            float newY = Mathf.Lerp(initialPosition.y, targetPosition.y, elapsedTime / animationDuration);
            float newZ = Mathf.Lerp(initialPosition.z, targetPosition.z, elapsedTime / animationDuration);

            piece.transform.position = new Vector3(piece.transform.position.x, newY, newZ);
            elapsedTime += Time.deltaTime;
            yield return null;
        }
        piece.transform.position = targetPosition;
    }

    int DetermineWinner(int p1Score, int p2Score){
        if(p1Score == p2Score){
            return -1;
        }else if(p1Score > p2Score){
            return 1;
        }else{
            return 0;
        }
    }

}