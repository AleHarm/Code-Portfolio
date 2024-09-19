#!/usr/bin/python3
import math

from which_pyqt import PYQT_VER

if PYQT_VER == 'PYQT5':
    from PyQt5.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT4':
    from PyQt4.QtCore import QLineF, QPointF
elif PYQT_VER == 'PYQT6':
    from PyQt6.QtCore import QLineF, QPointF
else:
    raise Exception('Unsupported Version of PyQt: {}'.format(PYQT_VER))

import random

# Used to compute the bandwidth for banded version
MAXINDELS = 3

# Used to implement Needleman-Wunsch scoring
MATCH = -3
INDEL = 5
SUB = 1

LEFT = 0
DIAGONAL = 1
UP = 2


class GeneSequencing:

    def __init__(self):
        pass

    # This is the method called by the GUI.
    # _seq1_ and _seq2_ are two sequences to be aligned,
    # _banded_ is a boolean that tells you whether you should compute a banded alignment or full alignment,
    # _align_length_ tells you how many base pairs to use in computing the alignment

    def align(self, seq1, seq2, banded, align_length):

        def BandedDistancesTableSetup():
            newDistances = [[(0, -1) for _ in range((2 * MAXINDELS) + 1)] for _ in range(self.MaxCharactersToAlign)]

            # Set top row values
            for num in range(4):
                j = num + 3
                newDistances[0][j] = (5 * num, -1)

            # Set diagonal top row values
            for num in range(4):
                j = 3 - num
                newDistances[num][j] = (5 * num, -1)

            # Set top left empty triangle values
            for i in range(MAXINDELS):
                for j in range(MAXINDELS - i):
                    newDistances[i][j] = (-1, -1)

            return newDistances

        def UnrestrictedDistancesTableSetup():
            newDistances = [[(0, -1) for _ in range(self.MaxCharactersToAlign)] for _ in
                            range(self.MaxCharactersToAlign)]

            for i in range(self.MaxCharactersToAlign):
                newDistances[i][0] = (i * 5, -1)

            for j in range(self.MaxCharactersToAlign):
                newDistances[0][j] = (j * 5, -1)

            return newDistances

        def Banded(seq1, seq2):

            if (len(seq1) < self.MaxCharactersToAlign or len(seq2) < self.MaxCharactersToAlign) and abs(
                    len(seq1) - len(seq2)) > 3:
                return False

            for i in range(1, self.MaxCharactersToAlign):
                isInGoofyRange = i < 4

                if isInGoofyRange:
                    j = 4 - i
                else:
                    j = 0

                while j < ((2 * MAXINDELS) + 1):

                    if isInGoofyRange:
                        seq2Index = j - (4 - i)  # Isolates the incremental nature of j
                    else:
                        seq2Index = j + (i - 4)  # Adds the number of rows since the goofy range

                    # Use null value if comparison string is too long
                    if (i - 1) < len(seq1):
                        iChar = seq1[i - 1]
                    else:
                        iChar = None

                    # Use null value if comparison string is too long
                    if (seq2Index) < len(seq2):
                        jChar = seq2[seq2Index]
                    else:
                        jChar = None

                    isMatch = iChar == jChar

                    if isMatch:
                        # If align_length is larger than both words, then grabbing the diagonal does not increase score
                        if iChar is None and jChar is None:
                            diagonalValue = distances[i - 1][j][0]
                        else:
                            diagonalValue = -3 + distances[i - 1][j][0]
                    else:
                        diagonalValue = 1 + distances[i - 1][j][0]

                    if (j + 1) < (2 * MAXINDELS + 1):
                        upValue = 5 + distances[i - 1][j + 1][0]
                    else:
                        upValue = math.inf
                    if (j - 1) >= 0:
                        leftValue = 5 + distances[i][j - 1][0]
                    else:
                        leftValue = math.inf

                    if leftValue <= upValue and leftValue <= diagonalValue:
                        distances[i][j] = (leftValue, LEFT)
                    elif upValue <= leftValue and upValue <= diagonalValue:
                        distances[i][j] = (upValue, UP)
                    else:
                        distances[i][j] = (diagonalValue, DIAGONAL)

                    j += 1
            return True

        def Unrestricted(seq1, seq2):
            for i in range(1, self.MaxCharactersToAlign):
                for j in range(1, self.MaxCharactersToAlign):
                    # Use null value if comparison string is too long
                    if (i - 1) < len(seq1):
                        iChar = seq1[i - 1]
                    else:
                        iChar = None

                    # Use null value if comparison string is too long
                    if (j - 1) < len(seq2):
                        jChar = seq2[j - 1]
                    else:
                        jChar = None
                    isMatch = iChar == jChar

                    if isMatch:
                        # If align_length is larger than both words, then grabbing the diagonal does not increase score
                        if iChar == None and jChar == None:
                            diagonalValue = distances[i - 1][j - 1][0]
                        else:
                            diagonalValue = -3 + distances[i - 1][j - 1][0]
                    elif iChar != None and jChar != None:
                        diagonalValue = 1 + distances[i - 1][j - 1][0]
                    else:
                        diagonalValue = math.inf

                    upValue = 5 + distances[i - 1][j][0]
                    leftValue = 5 + distances[i][j - 1][0]

                    if leftValue <= upValue and leftValue <= diagonalValue:
                        distances[i][j] = (leftValue, LEFT)
                    elif upValue <= leftValue and upValue <= diagonalValue:
                        distances[i][j] = (upValue, UP)
                    else:
                        distances[i][j] = (diagonalValue, DIAGONAL)

        def DetermineUnrestrictedAlignmentStrings(seq1, seq2):
            newSeq1 = ""
            newSeq2 = ""

            if len(seq1) < self.MaxCharactersToAlign:
                i = len(seq1)
            else:
                i = self.MaxCharactersToAlign - 1

            if len(seq2) < self.MaxCharactersToAlign:
                j = len(seq2)
            else:
                j = self.MaxCharactersToAlign - 1

            currentValue = distances[i][j][1]

            while currentValue != -1:

                if currentValue == DIAGONAL:
                    newSeq1 = seq1[i - 1] + newSeq1
                    newSeq2 = seq2[j - 1] + newSeq2
                    i -= 1
                    j -= 1
                elif currentValue == UP:
                    newSeq1 = seq1[i - 1] + newSeq1
                    newSeq2 = "-" + newSeq2
                    i -= 1
                else:
                    newSeq1 = "-" + newSeq1
                    newSeq2 = seq2[j - 1] + newSeq2
                    j -= 1

                currentValue = distances[i][j][1]

            return newSeq1, newSeq2

        def DetermineBandedAlignmentStrings(seq1, seq2):
            newSeq1 = ""
            newSeq2 = ""

            if len(seq1) < self.MaxCharactersToAlign:
                i = len(seq1)
            else:
                i = self.MaxCharactersToAlign - 1

            if len(seq2) < self.MaxCharactersToAlign:

                j = MAXINDELS + (len(seq2) - len(seq1))
                seq2Index = len(seq2) - 1
            else:
                j = MAXINDELS
                seq2Index = self.MaxCharactersToAlign - 2

            currentValue = distances[i][j][1]

            while currentValue != -1:

                if currentValue == DIAGONAL:
                    newSeq1 = seq1[i - 1] + newSeq1
                    newSeq2 = seq2[seq2Index] + newSeq2
                    i -= 1
                    seq2Index -= 1
                elif currentValue == UP:
                    newSeq1 = seq1[i - 1] + newSeq1
                    newSeq2 = "-" + newSeq2
                    i -= 1
                    j += 1
                else:
                    newSeq1 = "-" + newSeq1
                    newSeq2 = seq2[seq2Index] + newSeq2
                    j -= 1
                    seq2Index -= 1

                currentValue = distances[i][j][1]

            return newSeq1, newSeq2

        self.banded = banded
        self.MaxCharactersToAlign = align_length + 1

        if banded:
            distances = BandedDistancesTableSetup()
            isAlignmentPossible = Banded(seq1, seq2)
            if isAlignmentPossible:

                if len(seq1) < self.MaxCharactersToAlign:
                    rowNumber = len(seq1) + 1
                else:
                    rowNumber = self.MaxCharactersToAlign - 1

                if len(seq2) < self.MaxCharactersToAlign:

                    colNumber = MAXINDELS + (len(seq2) - len(seq1))
                else:
                    colNumber = MAXINDELS

                score = distances[rowNumber][colNumber][0]
                alignment1, alignment2 = DetermineBandedAlignmentStrings(seq1, seq2)
            else:
                score = math.inf
                alignment1 = alignment2 = "No Alignment Possible"
        else:
            distances = UnrestrictedDistancesTableSetup()
            Unrestricted(seq1, seq2)

            if len(seq1) < self.MaxCharactersToAlign:
                rowNumber = len(seq1)
            else:
                rowNumber = self.MaxCharactersToAlign - 1

            if len(seq2) < self.MaxCharactersToAlign:
                colNumber = len(seq2)
            else:
                colNumber = self.MaxCharactersToAlign - 1

            score = distances[rowNumber][colNumber][0]

            alignment1, alignment2 = DetermineUnrestrictedAlignmentStrings(seq1, seq2)

        alignment1 = alignment1[:100]
        alignment2 = alignment2[:100]
        return {'align_cost': score, 'seqi_first100': alignment1, 'seqj_first100': alignment2}
