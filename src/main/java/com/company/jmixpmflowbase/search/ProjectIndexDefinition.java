package com.company.jmixpmflowbase.search;

import com.company.jmixpmflowbase.entity.Project;
import io.jmix.core.DataManager;
import io.jmix.core.Id;
import io.jmix.search.index.annotation.AutoMappedField;
import io.jmix.search.index.annotation.IndexablePredicate;
import io.jmix.search.index.annotation.JmixEntitySearchIndex;

import java.util.function.Predicate;

@JmixEntitySearchIndex(entity = Project.class)
public interface ProjectIndexDefinition {

    @AutoMappedField(includeProperties = {"name", "manager.username", "tasks.name"})
    @AutoMappedField(includeProperties = {"attachment"}, analyzer = "english")
    void mapping();

    @IndexablePredicate
    default Predicate<Project> predicate(DataManager dataManager) {
        return (project) -> {
            Id<Project> id = Id.of(project);
            Project proj = dataManager.load(id).fetchPlanProperties("attachment").one();
            return proj.getAttachment() != null;
        };
    }
}
