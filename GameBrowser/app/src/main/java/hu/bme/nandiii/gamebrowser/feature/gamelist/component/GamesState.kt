package hu.bme.nandiii.gamebrowser.feature.gamelist.component

data class GamesState(
    val dropdownListState: ListState = ListState.ONSALE,
    val sortBy: SortState = SortState.DEFAULT,
    val sortDirection: SortDirection = SortDirection.ASC,
    val searchText: String = "",
    val searchActive: Boolean = false,
    val searchHistory: List<String> = emptyList(),
    val dropdownExpanded: Boolean = false,

    )