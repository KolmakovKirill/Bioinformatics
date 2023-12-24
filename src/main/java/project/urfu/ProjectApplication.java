package project.urfu;

import project.urfu.di.DI;

public class ProjectApplication
{
    public static void main(String[] args)
    {
        ProjectRunner projectRunner = new ProjectRunner(new DI());
        projectRunner.run();
    }
}
