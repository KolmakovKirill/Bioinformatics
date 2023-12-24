package project.urfu;

import project.urfu.data.DataInitializer;
import project.urfu.di.DI;

public class ProjectRunner
{
    private final DI di;

    public ProjectRunner(DI di)
    {
        this.di = di;
    }

    public void run()
    {
        DataInitializer dataInitializer = new DataInitializer(di);
        dataInitializer.initializeDataFromCsvFileIfDBEmpty();

        di.getAnalyticService().analyticDbData();
    }
}
