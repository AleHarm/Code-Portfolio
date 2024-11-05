using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HUDPvC : MonoBehaviour
{
    public Animator p1ScoreAnimator;
    public Animator CPUScoreAnimator;
    public bool isPlayerTurn = true;

    private GameObject p1WinText; 
    private GameObject CPUWinText;
    private GameObject tieText;
    private GameObject winBG; 
    private GameObject playAgainButton; 
    private GameObject startMenuButton;

    void Start(){

        p1WinText = this.transform.Find("Player wins").gameObject;
        CPUWinText = this.transform.Find("CPU wins").gameObject;
        tieText = this.transform.Find("Tie").gameObject;
        winBG = this.transform.Find("Winner Text BG").gameObject;
        playAgainButton = this.transform.Find("Play again button").gameObject; 
        startMenuButton = this.transform.Find("Start menu button").gameObject;
    }

    public void ManageTurns(bool isPlayerTurnNew){

        isPlayerTurn = isPlayerTurnNew;
        HighlightScores();
    }

    public void FadeInWinUI(int didPlayer1Win){

        if(didPlayer1Win == 1){
            p1WinText.GetComponent<Animator>().SetBool("IsFadedIn", true);
        }else if(didPlayer1Win == 0){
            CPUWinText.GetComponent<Animator>().SetBool("IsFadedIn", true);
        }else{
            tieText.GetComponent<Animator>().SetBool("IsFadedIn", true);    
        }
        winBG.GetComponent<Animator>().SetBool("IsFadedIn", true);
        playAgainButton.GetComponent<Animator>().SetBool("IsFadedIn", true);
        startMenuButton.GetComponent<Animator>().SetBool("IsFadedIn", true);
    }

    public void FadeOutWinUI(){
        
        p1WinText.GetComponent<Animator>().SetBool("IsFadedIn", false);
        CPUWinText.GetComponent<Animator>().SetBool("IsFadedIn", false);
        tieText.GetComponent<Animator>().SetBool("IsFadedIn", false); 
        winBG.GetComponent<Animator>().SetBool("IsFadedIn", false);
        playAgainButton.GetComponent<Animator>().SetBool("IsFadedIn", false);
        startMenuButton.GetComponent<Animator>().SetBool("IsFadedIn", false);
    }

    void HighlightScores(){

        if(isPlayerTurn){
            bool p1AnimatorState = p1ScoreAnimator.GetBool("MyTurn");

            if(p1AnimatorState){
                p1ScoreAnimator.SetTrigger("MyTurnAgain");
            }else{
                TriggerAnimationsForTurnChange(p1ScoreAnimator, CPUScoreAnimator);
            }
        }else{
            bool p2AnimatorState = CPUScoreAnimator.GetBool("MyTurn");    

            if(p2AnimatorState){
                    CPUScoreAnimator.SetTrigger("MyTurnAgain");
            }else{
                TriggerAnimationsForTurnChange(CPUScoreAnimator, p1ScoreAnimator);
            }
        }
    }

    void TriggerAnimationsForTurnChange(Animator currentTurnAnimator, Animator lastTurnAnimator){

        currentTurnAnimator.SetBool("MyTurn", true);
        lastTurnAnimator.SetBool("MyTurn", false);
    }
}
