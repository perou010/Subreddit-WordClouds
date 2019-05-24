import praw
import sqlite3
import datetime
import sys

reddit = praw.Reddit(client_id = 'JaydUvl7POUphw', client_secret = 'cidTUcnUG00Gx8YsAD1ECvCcKLo',user_agent = "...")

def GetPostTitles(subredditName):
    textPosts = [];
    for submission in reddit.subreddit(subredditName).top(time_filter = 'month', limit = 500):
        textPosts.append(submission.title)
    return textPosts

def cleanTitles(titleList):
    for title in titleList:
        for badChar in "),(.[]=123456789:":
            title.replace(badChar,"")

def writeTitles(filename):
    textPostList = GetPostTitles(filename);
    cleanTitles(textPostList)
    f = open("/home/ryan/SubWordClouds/"+filename,'w')
    f.write(filename + " ")
    for title in textPostList:
        f.write(title+" ")
    f.close()

#writeTitles('space')
writeTitles(sys.argv[1])



# table_name = 'SubDataTable'
#
# conn = sqlite3.connect(table_name)
# crsr = conn.cursor()
# #crsr.execute("create table SubredditData (name VARCHAR(30), subcount INT, date date)");
# conn.commit()
#
# subreddits = GetRecentSubreddits()
#
# for subreddit in subreddits:
#     name = subreddit
#     subcount = subreddits[subreddit]
#     sqlcmd = "INSERT INTO {} VALUES({},{})".format(table_name, name, subcount) #datetime.datetime.now().__str__())
#     print(sqlcmd)
#     crsr.execute(sqlcmd)
# conn.commit()
