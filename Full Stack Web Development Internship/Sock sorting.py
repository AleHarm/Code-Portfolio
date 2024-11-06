
socks = [1,2,2,1,1,3,5,1,4,4,1,2,3,2,1,2,3,4,5,4,4,4,4,5,4,3,2,1,1,1,1,2,3,2,3,4,5,5,5,4,3,2,1,2,2,2,3,3,3,2,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5]
colors = {'yellow':0,'blue':0,'red':0,'green':0,'orange':0}

yellow = blue = red = green = orange = 0

for item in socks:
    match item:
        case 1:
            yellow += 1
        case 2:
            blue += 1
        case 3:
            red += 1
        case 4:
            green += 1
        case 5:
            orange += 1

print("Yellow socks: {}\nBlue socks: {}\nRed socks: {}\nGreen socks: {}\nOrange socks: {}".format(yellow, blue, red, green, orange))

colors['yellow'] = yellow
colors['blue'] = blue
colors['red'] = red
colors['green'] = green
colors['orange'] = orange

for key, value in colors.items():
    if (value % 2) == 0:
        print("There are " + str(value/2) + " pairs of " + key + " socks\n")
    else:
        print("There are " + str((value - 1)/2) + " pairs of " + key + " socks, with 1 left over\n")