package com.company.jmixpmflowbase.view.project;

import com.company.jmixpmflowbase.entity.Project;

import com.company.jmixpmflowbase.view.main.MainView;

import com.vaadin.flow.router.Route;
import io.jmix.core.Id;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import io.jmix.search.index.EntityIndexer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "projects", layout = MainView.class)
@ViewController("Project.list")
@ViewDescriptor("project-list-view.xml")
@LookupComponent("projectsDataGrid")
@DialogMode(width = "64em")
public class ProjectListView extends StandardListView<Project> {

    @ViewComponent
    private CollectionContainer<Project> projectsDc;
    @Autowired
    private EntityIndexer entityIndexer;
    @Autowired
    private Notifications notifications;
    @ViewComponent
    private DataGrid<Project> projectsDataGrid;

    @Subscribe("projectsDataGrid.indexAll")
    public void onProjectsDataGridIndexAll(final ActionPerformedEvent event) {
        List<Id<?>> ids = projectsDc.getItems()
                .stream()
                .map(Id::of)
                .collect(Collectors.toList());
        entityIndexer.indexCollectionByEntityIds(ids);
        notifications.show(ids.size() + " Projects were indexed");
    }

    @Subscribe("projectsDataGrid.indexSelected")
    public void onProjectsDataGridIndexSelected(final ActionPerformedEvent event) {
        Project selected = projectsDataGrid.getSingleSelectedItem();
        if (selected != null) {
            entityIndexer.indexByEntityId(Id.of(selected));

            notifications.show(selected.getName() + " project was indexed");
        }
    }
}