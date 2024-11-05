using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RotateSkybox : MonoBehaviour
{

    public Skybox box;

    void Update () {
        box.material.SetFloat("_Rotation", Time.time * .5f);
    }
}
