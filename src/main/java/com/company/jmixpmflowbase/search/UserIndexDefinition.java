package com.company.jmixpmflowbase.search;

import com.company.jmixpmflowbase.entity.User;
import io.jmix.search.index.annotation.AutoMappedField;
import io.jmix.search.index.annotation.JmixEntitySearchIndex;

@JmixEntitySearchIndex(entity = User.class)
public interface UserIndexDefinition {

    @AutoMappedField(
            includeProperties = {"*"},
            excludeProperties = {"password"}
    )
    void mapping();
}
