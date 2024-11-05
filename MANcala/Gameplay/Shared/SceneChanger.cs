using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SceneChanger : MonoBehaviour
{

    public GameObject hinge;
    public Animator cameraAnimator;

    public void SwitchToStartMenu(){

        //Trigger backwards camera move
        cameraAnimator.SetTrigger("GoToStartMenu");
        //Trigger fold
        hinge.GetComponent<FoldUnfold>().Fold();
    }
}
