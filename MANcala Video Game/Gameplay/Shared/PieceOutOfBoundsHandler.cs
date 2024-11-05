using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PieceOutOfBoundsHandler : MonoBehaviour
{
    private void OnTriggerEnter(Collider collider){
        if(collider.transform.parent.parent.gameObject.name == "Ragdoll"){

            Transform ragdollRoot = collider.transform;
            ragdollRoot.position = ragdollRoot.parent.parent.parent.position;
        }
    }
}
