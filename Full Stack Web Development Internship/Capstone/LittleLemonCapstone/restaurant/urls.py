from django.urls import path 
from . import views
  
urlpatterns = [ 
    path('', views.index, name='index'),
    path('menu/', views.menuView.as_view()),
    path('menu_item/<int:pk>/', views.menuItemView.as_view()),
    path('booking/', views.bookingView.as_view()),
]