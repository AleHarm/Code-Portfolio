using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PointHole : MonoBehaviour{

    public int score;

    void Update(){

        score = this.transform.childCount - 1;
    }
}
