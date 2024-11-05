using UnityEngine;
using UnityEngine.SceneManagement;



public class FoldUnfold : MonoBehaviour
{
    public GameObject HUD;
    public GameObject gamePlayManagement;
    public GameObject holes;


    private Animator animator;
    private SceneLoader sceneLoader;
    private string sceneName;

    void Start()
    {
        animator = GetComponent<Animator>();
        sceneLoader = FindObjectOfType<SceneLoader>();
        sceneName = SceneManager.GetActiveScene().name;
    }

    void Update(){

        if(holes == null){
            if(sceneName == "PvP"){

                holes = GameObject.Find("Holes(Clone)");
            }else{

                holes = GameObject.Find("HolesPvC(Clone)");
            }
        }
    }

    public void Fold()
    {
        holes.SetActive(false);
        HUD.SetActive(false);
        animator.SetTrigger("TopBoardFold");
    }

    public void FoldFinished()
    {

        gamePlayManagement.GetComponent<SceneLoader>().LoadScene("Start Menu");
    }

    public void UnfoldFinished()
    {

        HUD.SetActive(true);
        holes.SetActive(true);
    }
}
