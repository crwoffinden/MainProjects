import json
import string
import argparse
import os

def main(inputString):
    parser = argparse.ArgumentParser("Word Count")
    parser.add_argument("--string", type=str, help="string to analyze")
    args = parser.parse_args(inputString)
    # Remember to save the dictionary as a json file named "word-counts.json"


    return None

if __name__ == "__main__":
    parser = argparse.ArgumentParser("Word Counter")
    parser.add_argument("-s","--string",type=str,required=True, help="Sentence to have the number of words counted")
    args = parser.parse_args()
    main(args.string)
    