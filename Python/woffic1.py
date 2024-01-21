import json
import string
import argparse
import os

def main(inputString):
    words = []
    wordList = []
    start = 0
    for i in range(len(inputString)):
        if string.whitespace.__contains__(inputString[i]):
            word = inputString[start:i]
            for x in string.punctuation:
                word = word.replace(x, '')
            word = word.lower() #FIXME may not work
            if words.__contains__(word):
                index = words.index(word)
                wordList[index]["count"] += 1 #FIXME may not work
            else:
                words.append(word)
                obj = {"word": word, "count": 1}
                wordList.append(obj)
            start = i + 1
    word = inputString[start:]
    for x in string.punctuation:
        word = word.replace(x, '')
    word = word.lower()  # FIXME may not work
    if words.__contains__(word):
        index = words.index(word)
        wordList[index]["count"] += 1  # FIXME may not work
    else:
        words.append(word)
        obj = {"word": word, "count": 1}
        wordList.append(obj)
    dictionary = {}
    for i in range(len(words)):
        dictionary[words[i]] = wordList[i]["count"]
    cwd = os.getcwd()
    fpath = os.path.join(cwd, "word-counts.json")
    with open(fpath, "w") as jsonFile:
        jsonObj = json.dumps(dictionary, indent=4)
        jsonFile.write(jsonObj) #FIXME may not be what you think        jsonFile.write(jsonObj)
    # Remember to save the dictionary as a json file named "word-counts.json"


    return None

if __name__ == "__main__":
    parser = argparse.ArgumentParser("Word Counter")
    parser.add_argument("-s","--string",type=str,required=True, help="Sentence to have the number of words counted")
    args = parser.parse_args()
    main(args.string)
    