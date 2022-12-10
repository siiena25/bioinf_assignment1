from typing import Callable


def get_score(a: str, b: str, match_score: int = 0, mismatch_score: int = 1) -> int:
    return match_score if a == b else mismatch_score


def set_distances(sequences):
    current_clusters = []
    distances = {}

    for i in range(len(sequences)):
        print(sequences[i])
        name = sequences[i]['name']
        current_clusters.append(sequences[i]['name'])
        distances[name] = {}

    for i in range(len(current_clusters)):
        for j in range(i + 1, len(current_clusters)):
            cluster1 = list(sequences)[i]
            cluster2 = list(sequences)[j]

            _, _, cluster_distance = NW(cluster1['s'], cluster2['s'])

            distances[cluster1['name']][cluster2['name']] = cluster_distance
            distances[cluster2['name']][cluster1['name']] = cluster_distance

    return current_clusters, distances

def calc(sequences):
    c_c, distances = set_distances(sequences)
    cluster_count = {}

    print()
    for i in distances:
        print(distances[i])

    for c in c_c:
        cluster_count[c] = 1

    print()

    while len(c_c) != 1:
        closest1 = c_c[0]
        closest2 = c_c[1]
        closest_dist = float("inf")

        for i in range(len(c_c)):
            for j in range(i + 1, len(c_c)):
                if distances[c_c[i]][c_c[j]] < closest_dist:
                    closest1 = c_c[i]
                    closest2 = c_c[j]
                    closest_dist = distances[closest1][closest2]

        new_claster_name = f"{closest1},{closest2}"
        cluster_count[new_claster_name] = cluster_count[closest1] + cluster_count[closest2]
        print(f"{new_claster_name}: {closest_dist/2}")

        distances[new_claster_name] = {}

        for c in c_c:
            if c != closest2 and c != closest1:
                count1 = cluster_count[closest1]
                count2 = cluster_count[closest2]

                new_dist = float(count1 * distances[closest1][c] + count2 * distances[closest2][c]) / (count1 + count2)

                distances[new_claster_name][c] = new_dist
                distances[c][new_claster_name] = new_dist

        c_c.remove(closest2)
        c_c.remove(closest1)
        c_c.append(new_claster_name)


def NW(seq1: str, seq2: str, score_fun: Callable = get_score, gap_score: int = 2):

    m, n = len(seq1) + 1, len(seq2) + 1

    matrix = [[0] * n for _ in range(m)]

    for i in range(m):
        matrix[i][0]  = i * gap_score
    for j in range(n):
        matrix[0][j] = j * gap_score

    for i in range(1, m):
        for j in range(1, n):
            matrix[i][j] = min(matrix[i - 1][j - 1] + score_fun(seq1[i - 1], seq2[j - 1]),
                               matrix[i - 1][j] + gap_score,
                               matrix[i][j - 1] + gap_score)

    score = matrix[-1][-1]
    i, j = m - 1, n - 1
    aln1 = ""
    aln2 = ""
    while i > 0 or j > 0:
        a, b = '-', '-'

        if i > 0 and j > 0 and matrix[i][j] == matrix[i-1][j-1] + score_fun(seq1[i - 1], seq2[j - 1]):
            a = seq1[i - 1]
            b = seq2[j - 1]
            i -= 1
            j -= 1

        elif i > 0 and matrix[i][j] == matrix[i - 1][j] + gap_score:
            a = seq1[i - 1]
            i -= 1

        elif j > 0 and matrix[i][j] == matrix[i][j - 1] + gap_score:
            b = seq2[j - 1]
            j -= 1

        aln1 += a
        aln2 += b

    return aln1[::-1], aln2[::-1], score


def print_array(matrix: list):
    for row in matrix:
        for element in row:
            print(f"{element:6}", end="")
        print()


def read_fasta(path):
    f = open(path, 'r')
    lines = f.readlines()
    sequences = []
    i = 0
    pair = {}

    while i < len(lines):
        if lines[i][0] == '>':
            if i > 0:
                sequences.append({'name': pair['name'], 's': pair['s']})
            name = ""
            j = 1
            while j < len(lines[i]) and lines[i][j] != ' ' and lines[i][j] != '\n':
                name += lines[i][j]
                j += 1
            pair['name'] = name
            pair['s'] = ''
            i += 1
        elif len(lines[i]) == 0 or lines[i][0] == ';' or lines[i][0] == '\n':
            i += 1
        else:
            pair['s'] = lines[i][:-1]
            i += 1
    sequences.append({'name': pair['name'], 's': pair['s']})
    return sequences


if __name__ == "__main__":
    fasta_path = "large.fasta"
    seq = read_fasta(fasta_path)
    calc(seq)
