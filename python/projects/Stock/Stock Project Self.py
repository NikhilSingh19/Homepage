import json
import urllib.request

def get_stock_quote(ticker_symbol):
    url = 'http://finance.google.com/finance/info?q='+ticker_symbol
    lines = urllib.request.urlopen(url).readlines()
    list_of_strings = []
    for line in lines:
        list_of_strings.append(line.decode('utf-8').strip('\n'))
    merged_string = ''
    for string in list_of_strings:
        if string not in ('// [', ']'):
            merged_string += string
    return json.loads(merged_string)

x=1

while x==1:
    symbol=input("Enter ticker symbol or Type Quit to Quit the Program: ")
    if(symbol.lower()=="quit"):
        break
    try:
        quote = get_stock_quote(symbol)
        print ('ticker:',quote['t'])
        print ('current price:', quote['l_cur'])
        print ('price change:',"$"+str(quote['c']))
        print ('percent change:',str(quote['cp'])+"%")
    except urllib.error.HTTPError:
        print("Not a stock ticker, try again")

print("The most expensive stock's ticker is",highest,"and it costs",mostexpensive,"dollars")
