public class NBody {
    public static double readRadius(String path) {
        In in = new In(path);
        int plantNumbers = in.readInt();
        double radius = in.readDouble();
        return radius;
    }

    public static Body[] readBodies(String path) {
        In in = new In(path);
        int plantNumbers = in.readInt();
        in.readDouble();
        Body[] Bodys = new Body[plantNumbers];
        int index = 0;
        while (index != plantNumbers) {
            double xP = in.readDouble();
            double yP = in.readDouble();
            double xV = in.readDouble();
            double yV = in.readDouble();
            double m = in.readDouble();
            String img = in.readString();
            Bodys[index] = new Body(xP,yP,xV,yV,m,img);
            index += 1;
        }
        return Bodys;
    }

    public static void main(String[] args) {
        /**
         * arg[0]   T
         * arg[1]   dt
         * arg[2]   filename
         * Radius   the scale of the picture
         * Bodies   arrays of the object body
         */
        Double T = Double.valueOf(args[0]);
        Double dt = Double.valueOf(args[1]);
        String filename = args[2];
        double Radius = NBody.readRadius(filename);
        Body[] Bodies = NBody.readBodies(filename);

        /** draw */
        StdDraw.setScale(-Radius, Radius);

        /* Clears the drawing window. */
        StdDraw.clear();

        /* Set the background. */
        String background = "images/starfield.jpg";
        StdDraw.picture(0, 0, background);

        /* Set the plants. */
        for (Body body : Bodies) {
            body.draw();
        }

        /** Animation */
        /* prevent flickering in the animation. */
        StdDraw.enableDoubleBuffering();

        double time = 0;
        while (time < T) {
            double [] xForces = new double[Bodies.length];
            double [] yForces = new double[Bodies.length];
            for (int i =0; i < Bodies.length; i++) {
                xForces[i] = Bodies[i].calcNetForceExertedByX(Bodies);
                yForces[i] = Bodies[i].calcNetForceExertedByY(Bodies);
            }
            for (int i = 0; i< xForces.length; i++) {
                Bodies[i].update(dt,xForces[i],yForces[i]);
            }
            /* Draw the background image. */
            StdDraw.picture(0, 0, background);
            /* Draw all of the Bodys. */
            for (Body body : Bodies) {
                body.draw();
            }
            /* Show the offscreen buffer (see the show method of StdDraw).*/
            StdDraw.show();
            StdDraw.pause(10);
            time += dt;
        }


        StdDraw.show();
    }

}