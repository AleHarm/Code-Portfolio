from django.http import HttpResponse 
from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from . import models
from . import serializers


def index(request):
 return render(request, 'index.html', {})

class bookingView(APIView):
    def post(self, request):
        if self.request.user.is_authenticated:
            serializer = serializers.BookingSerializer(data=request.data)
            
            if serializer.is_valid():
                serializer.save()
                return Response({"status":"success", "data": serializer.data})
            
        else:
            return HttpResponse("<h1>Authentication credentials were not provided</h1>",status='401')

    def get(self, request):
        items = models.Booking.objects.all()
        serializer = serializers.BookingSerializer(items, many = True)
        return Response(serializer.data)

class menuView(APIView):
    def post(self, request):
        if self.request.user.is_authenticated:
            serializer = serializers.MenuSerializer(data=request.data)
            
            if serializer.is_valid():
                serializer.save()
                return Response({"status":"success", "data": serializer.data})
            
        else:
            return HttpResponse("<h1>Authentication credentials were not provided</h1>",status='401')
    
    def get(self, request):
        items = models.Menu.objects.all()
        serializer = serializers.MenuSerializer(items, many = True)
        return Response(serializer.data)


class menuItemView(APIView):
    def get(self, request, pk=None):
        if pk:
            menu_item = models.Menu.objects.get(pk=pk)

        else:
            menu_item = ""

        serializer = serializers.MenuSerializer(menu_item)
        return Response(serializer.data)

