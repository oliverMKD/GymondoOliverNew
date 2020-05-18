"# gymondo" 

This test app for Gymondo is done in Kotlin with MVVM architectural design pattern with Room persistence library,
ViewModel and LiveData, Data Binding and Paging Library, Dagger 2 for DI.
The idea is to get the data from server using Retrofit2 and Kotlin Coroutines.
Store the data in Room and getting it as LiveData.
Data Binding is used for displaying the data in the UI layer.
As RecyclerView adapters, ListAdapter and PagedListAdapter are used. Less boilerplate code and with the DiffUtil.ItemCallback we are allways sure about the items displayed.
For Search, Filter and DetailsFragment, queries from Room are used, becouse all the data is inserted.

Biggest problem for me ( and it's not done as it should be) and I thought it was supposed to be the easiest part and left it for the end, is paggination of the lists.
That is why I used PagedListAdapter and I know there is BoundaryCallback for this, but I didn't implement it :(


