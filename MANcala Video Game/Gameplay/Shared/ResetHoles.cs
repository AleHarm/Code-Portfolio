using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ResetHoles : MonoBehaviour
{

    public GameObject holesPrefab;
    public GameObject holes;
    public Transform HUD;

    private string sceneName;
    private bool isPvC = false;

    private void Start()
    {
        sceneName = SceneManager.GetActiveScene().name;

        if (sceneName == "PvC")
        {
            isPvC = true;
        }
    }

    public void ResetGame(){
        HUD = this.transform.parent;
        if(isPvC){
            HUD.GetComponent<HUDPvC>().isPlayerTurn = true;
            holes.GetComponent<HolesPvC>().isPlayerTurn = true;
            HUD.GetComponent<HUDPvC>().ManageTurns(true);
            ResetHolesPrefab();
            HUD.GetComponent<HUDPvC>().FadeOutWinUI();
        }else{

            HUD.GetComponent<HUD>().isPlayer1Turn = true;
            holes.GetComponent<Holes>().isPlayer1Turn = true;
            HUD.GetComponent<HUD>().ManageTurns(true);
            ResetHolesPrefab();
            HUD.GetComponent<HUD>().FadeOutWinUI();
        }
        holes.SetActive(true);
    }

    void ResetHolesPrefab(){
        Transform parentTransform = holes.transform.parent;
        Vector3 newPosition = parentTransform.position;
        newPosition.y += 4f;

        Quaternion newRotation = Quaternion.Euler(0f, parentTransform.rotation.eulerAngles.y + 180f, 0f);

        Destroy(holes);
        // Instantiate the prefab with the updated position and rotation
        holes = Instantiate(holesPrefab, newPosition, newRotation, parentTransform);
    }

}