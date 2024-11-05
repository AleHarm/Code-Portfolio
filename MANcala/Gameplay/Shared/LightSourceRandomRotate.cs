using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightSourceRandomRotate : MonoBehaviour
{
    void Update () {
        transform.Rotate(.02f, .01f, .015f, Space.Self);
    }
}
