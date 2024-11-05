using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

public class Score : MonoBehaviour
{
    public TextMeshProUGUI text;
    public int score = 0;

    private GameObject pointHole;
    private string nameString;

    void Start(){

        nameString = this.gameObject.name;
        AssignPointHole();
    }

    void Update(){

        if(pointHole == null){
            AssignPointHole();
        }

        if(pointHole != null){

            int oldScore = score;
            score = pointHole.GetComponent<PointHole>().score;

            if(score != oldScore){
                UpdateScore();
            }
        }
    }

    public void UpdateScore(){

        if(nameString == "P1 Score"){

            text.text = "Player 1\n" + score;
        }else if(nameString == "P2 Score"){

            text.text = "Player 2\n" + score;
        }else if(nameString == "Player Score"){

            text.text = "Player\n" + score;
        }else{

            text.text = "CPU\n" + score;
        }
    }

    void AssignPointHole(){
        if(nameString == "P1 Score"){
            pointHole = GameObject.Find("Player 1 Point Hole");
        }else if(nameString == "P2 Score"){
            pointHole = GameObject.Find("Player 2 Point Hole");
        }else if(nameString == "Player Score"){
            pointHole = GameObject.Find("Player Point Hole");
        }else{
            pointHole = GameObject.Find("CPU Point Hole");
        }
    }   
}
