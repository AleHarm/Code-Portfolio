using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HUD : MonoBehaviour
{
    public Animator p1ScoreAnimator;
    public Animator p2ScoreAnimator;
    public Animator cameraAnimator;
    public bool isPlayer1Turn = true;

    private GameObject p1WinText; 
    private GameObject p2WinText;
    private GameObject tieText;
    private GameObject winBG; 
    private GameObject playAgainButton;
    private GameObject startMenuButton;

    void Start(){

        p1WinText = this.transform.Find("Player 1 wins").gameObject;
        p2WinText = this.transform.Find("Player 2 wins").gameObject;
        tieText = this.transform.Find("Tie").gameObject;
        winBG = this.transform.Find("Winner Text BG").gameObject;
        playAgainButton = this.transform.Find("Play again button").gameObject;
        startMenuButton = this.transform.Find("Start menu button").gameObject;
    }

    public void ManageTurns(bool isPlayer1TurnNew){

        isPlayer1Turn = isPlayer1TurnNew;
        HighlightScores();
    }

    public void FadeInWinUI(int didPlayer1Win){

        if(didPlayer1Win == 1){
            p1WinText.GetComponent<Animator>().SetBool("IsFadedIn", true);
        }else if(didPlayer1Win == 0){
            p2WinText.GetComponent<Animator>().SetBool("IsFadedIn", true);
        }else{
            tieText.GetComponent<Animator>().SetBool("IsFadedIn", true);    
        }
        winBG.GetComponent<Animator>().SetBool("IsFadedIn", true);
        playAgainButton.GetComponent<Animator>().SetBool("IsFadedIn", true);
        startMenuButton.GetComponent<Animator>().SetBool("IsFadedIn", true);
    }

    public void FadeOutWinUI(){
        
        p1WinText.GetComponent<Animator>().SetBool("IsFadedIn", false);
        p2WinText.GetComponent<Animator>().SetBool("IsFadedIn", false);
        tieText.GetComponent<Animator>().SetBool("IsFadedIn", false); 
        winBG.GetComponent<Animator>().SetBool("IsFadedIn", false);
        playAgainButton.GetComponent<Animator>().SetBool("IsFadedIn", false);
        startMenuButton.GetComponent<Animator>().SetBool("IsFadedIn", false);
    }

    void HighlightScores(){

        if(isPlayer1Turn){
            bool p1AnimatorState = p1ScoreAnimator.GetBool("MyTurn");

            if(p1AnimatorState){
                p1ScoreAnimator.SetTrigger("MyTurnAgain");
            }else{
                TriggerAnimationsForTurnChange(p1ScoreAnimator, p2ScoreAnimator);
            }
        }else{
            bool p2AnimatorState = p2ScoreAnimator.GetBool("MyTurn");    

            if(p2AnimatorState){
                    p2ScoreAnimator.SetTrigger("MyTurnAgain");
            }else{
                TriggerAnimationsForTurnChange(p2ScoreAnimator, p1ScoreAnimator);
            }
        }
    }

    void TriggerAnimationsForTurnChange(Animator currentTurnAnimator, Animator lastTurnAnimator){

        currentTurnAnimator.SetBool("MyTurn", true);
        lastTurnAnimator.SetBool("MyTurn", false);
        cameraAnimator.SetTrigger("ChangeTurn");
    }
}
