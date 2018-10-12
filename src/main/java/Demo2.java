import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class Demo2 implements GLEventListener {

    public static void main(String[] args) {
        GLProfile glp = GLProfile.get(GLProfile.GL2);

        GLCapabilities caps = new GLCapabilities(glp);

        GLWindow glWindow = GLWindow.create(caps);
        glWindow.setSize(800, 600);
        glWindow.setVisible(true);


        glWindow.addWindowListener(new WindowAdapter() {
            public void windowDestroyNotify(WindowEvent e) {
                System.exit(0);
            }
        });

        glWindow.getGL().getGL2().glColor3d(100, 100, 100);
        glWindow.getGL().getGL2().glBegin(GL2.GL_LINES);
        glWindow.getGL().getGL2().glVertex3f(0.50f,-0.50f,0);
        glWindow.getGL().getGL2().glVertex3f(-0.50f,0.50f,0);
        glWindow.getGL().getGL2().glEnd();
        glWindow.getGL().getGL2().glFlush();
//        glWindow.setSize(100, 100);


//        FPSAnimator animator = new FPSAnimator(glWindow, 60);
//        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
        System.out.println("hello");
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
