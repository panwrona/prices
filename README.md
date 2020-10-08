# prices

Things to change:

Since the task is demanding not to use any persistence, onSaveInstanceState etc, and the inteface is pretty straightforward and restricted, 
in my opinion the proper way to create the stream is to have the Presenter that survives the configuration changes. Since the Loaders are depracated because of the
Architecture Components and I couldn't use the MVVM pattern, I created a naive 'PresenterStore' to handle the configuration changes. What I would do if I had more time
is that I would maybe use dependency injection for restoring the Presenter or find another way.

Either way the Presenter is lifecycle aware, subscriptions are disposed and there shouldn't be Memory Leaks in the code.

Also I wanted to do it in `reactive` style and I didn't want to store the previous/current value anywhere but rather obtain it from the stream, that's why I proposed
this soultion in this manner.
