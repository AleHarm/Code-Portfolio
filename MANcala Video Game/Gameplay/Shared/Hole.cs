using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Hole : MonoBehaviour
{
    public int numPieces = 0;


    void Update(){

        numPieces = this.transform.childCount - 1;
    }
}
