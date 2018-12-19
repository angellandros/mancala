from __future__ import print_function
import json
import requests

def start(player1, player2, url='http://localhost:8080'):
    return requests.post('%s/start?player1=%s&player2=%s' % (url, player1, player2))


def play(player, index, url='http://localhost:8080'):
    return requests.put('%s/play?player=%s&index=%s' % (url, player, index))


def parse(body):
    board = json.loads(body)
    for player, row in sorted(board['board'].items(), key=lambda x: x[0]):
        print(' '.join(['%-2d' % cell['stones'] for cell in row]), player)
    return board['dran']


def main():
    player1 = input('Player 1 name: ')
    player2 = input('Player 2 name: ')
    player = parse(start(player1, player2).text)
    while True:
        code = 404
        while code != 200:
            index = input('Move for %s: ' % player)
            response = play(player, index)
            code = response.status_code
        player = parse(response.text)


if __name__ == '__main__':
    main()
