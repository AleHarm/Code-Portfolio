from typing import List


class Solution:
    def reconstructQueue(self, people: List[List[int]]) -> List[List[int]]:

        peopleSorted = sorted(people, key=lambda x: (x[0], -x[1]))
        reconstructedList = [None for _ in range(len(peopleSorted))]

        for index in range(len(peopleSorted)):
            currentPerson = peopleSorted[index]
            numEmpty = 0

            for spotIndex in range(len(reconstructedList)):
                if reconstructedList[spotIndex] == None:
                    if numEmpty == currentPerson[1]:
                        reconstructedList[spotIndex] = currentPerson
                        break
                    else:
                        numEmpty += 1

        return reconstructedList




sol = Solution()

people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
print(sol.reconstructQueue(people))