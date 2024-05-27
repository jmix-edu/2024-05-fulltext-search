package com.company.jmixpmflowbase.view.customsearch;


import com.company.jmixpmflowbase.entity.SearchResultItem;
import com.company.jmixpmflowbase.view.main.MainView;

import com.google.common.base.Strings;
import com.vaadin.flow.router.Route;
import io.jmix.core.Metadata;
import io.jmix.flowui.component.SupportsTypedValue;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.view.*;
import io.jmix.search.searching.EntitySearcher;
import io.jmix.search.searching.FieldHit;
import io.jmix.search.searching.SearchContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "custom-search-view", layout = MainView.class)
@ViewController("CustomSearchView")
@ViewDescriptor("custom-search-view.xml")
public class CustomSearchView extends StandardView {

    @ViewComponent
    private CollectionContainer<SearchResultItem> searchResultItemsDc;
    @Autowired
    private EntitySearcher entitySearcher;
    @Autowired
    private Metadata metadata;

    @Subscribe("searchField")
    public void onSearchFieldTypedValueChange(final SupportsTypedValue.TypedValueChangeEvent<TypedTextField<String>, String> event) {

        String value = event.getValue();
        if (Strings.isNullOrEmpty(value)) {
            searchResultItemsDc.getMutableItems().clear();
            return;
        }
        SearchContext searchContext = new SearchContext(value).setSize(100);
        List<SearchResultItem> dataGridItems = entitySearcher.search(searchContext)
                .getAllEntries().stream().map(resultEntry -> {
                    String fields = resultEntry.getFieldHits().stream()
                            .map(FieldHit::getFieldName)
                            .collect(Collectors.joining(", "));
                    return createSearchResultItem(resultEntry.getEntityName(),
                            resultEntry.getInstanceName(), resultEntry.getDocId(), fields);
                })
                .collect(Collectors.toList());
        searchResultItemsDc.setItems(dataGridItems);
    }

    private SearchResultItem createSearchResultItem(String entityName, String instanceName,
                                                    String instanceId, String fields) {
        SearchResultItem dataGridItem = metadata.create(SearchResultItem.class);
        dataGridItem.setEntityName(entityName);
        dataGridItem.setInstanceName(instanceName);
        dataGridItem.setInstanceId(instanceId);
        dataGridItem.setFields(fields);
        return dataGridItem;
    }
}