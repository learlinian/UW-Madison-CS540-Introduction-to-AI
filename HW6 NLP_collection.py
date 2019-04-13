from os import listdir
from os.path import join, isfile
from collections import Counter
import matplotlib.pyplot as plt
import numpy as np


# extract words from an individual document
def get_document_words(file):
    c = Counter()  # define world counter c
    f = file.read()
    f = f.replace('\n', ' ')
    while f:
        word = f.partition(' ')[0]  # get the word
        f = f.partition(' ')[2]  # get the string after the empty space
        if word != ' ' and word != '':
            c[word] += 1
    return c


# plot the graph: Question 3
def plot_graph(c):
    r = np.arange(len(c))
    r += 1
    word_count = [0] * len(c)
    index = 0

    for item in c.most_common(len(cnt)):
        word_count[index] = item[1]
        index += 1

    # linear plot
    plt.figure(1)
    plt.plot(r, word_count)
    plt.ylabel('Word Count')
    plt.xlabel('Rank')

    # log plot
    plt.figure(2)
    plt.plot(np.log(r), np.log(word_count))  # use e as base
    plt.ylabel('log(Word Count)')
    plt.xlabel('log(Rank)')
    plt.show()


def get_idf_count(word, counter_list):
    idf_count = 0

    for c_temp in counter_list:
        if c_temp[word] is not 0:
            idf_count += 1
    return idf_count


# calculate tf-idf: Question 4
def tf_idf(file, counter_list):
    global total_files

    # initialize tf, idf and tf_idf value for top ten words in txt file
    tf_idf_top_ten = [0.0] * 10

    c = get_document_words(file)

    tf_idf_all = [0.0] * len(c)           # initialize tf, idf and tf_idf value for all words

    # calculate tf-idf value for 'contract'
    tf_contract = c['contract'] / c.most_common(1)[0][1]                    # get tf value
    idf_count_contract = np.log10(total_files / get_idf_count('contract', counter_list))  # get idf value
    tf_idf_contract = tf_contract * idf_count_contract                      # get tf-idf value

    # calculate tf-idf value for top 10 words
    for i in range(10):
        tf_top_ten = c.most_common(10)[i][1] / c.most_common(1)[0][1]
        idf_top_ten = np.log10(total_files / get_idf_count(c.most_common(10)[i][0], counter_list))
        tf_idf_top_ten[i] = tf_top_ten * idf_top_ten

    # calculate tf-idf value for all words
    for i in range(len(c)):
        tf_all = c.most_common(len(c))[i][1] / c.most_common(1)[0][1]
        idf_all = np.log10(total_files / get_idf_count(c.most_common(len(c))[i][0], counter_list))
        tf_idf_all[i] = tf_all * idf_all

    return tf_idf_contract, tf_idf_top_ten, tf_idf_all


if __name__ == '__main__':
    cnt = Counter()
    cnt_temp = Counter()
    total_files = len(listdir('news'))
    counter_list = []

    for f in listdir('news'):
        if isfile('news/' + join(f)):
            file = open('news/' + join(f), 'r')
            cnt_temp = get_document_words(file)
            counter_list.append(cnt_temp)
        cnt += cnt_temp

    print('total words are : ' + str(sum(cnt.values())))
    print('distinct words are : ' + str(len(cnt)))
    print('Top 20 most common words are: ' + str(cnt.most_common(20)))

    plot_graph(cnt)

    counter_all_098 = get_document_words(open('news/098.txt', 'r'))
    counter_all_297 = get_document_words(open('news/297.txt', 'r'))

    tf_idf_contract_098, tf_idf_top_ten_098, tf_idf_all_098 = tf_idf(open('news/098.txt', 'r'), counter_list)
    tf_idf_contract_297, tf_idf_top_ten_297, tf_idf_all_297 = tf_idf(open('news/297.txt', 'r'), counter_list)
    print('tf_idf value for contract in file 098 is: ' + str(tf_idf_contract_098))
    print('tf_idf value for top ten values in file 098 are: ' + str(tf_idf_top_ten_098))

    # calculate Bag-of-Words vectors similarity
    repeated_words_counter = counter_all_098 & counter_all_297
    repeated_words_count_297 = []
    repeated_words_count_098 = []
    repeated_words = []
    count_all_297 = []
    word_all_297 = []
    count_all_098 = []
    word_all_098 = []

    for i in range(len(counter_all_297)):
        count_all_297.append(counter_all_297.most_common(len(counter_all_297))[i][1])
        word_all_297.append(counter_all_297.most_common(len(counter_all_297))[i][0])

    for i in range(len(counter_all_098)):
        count_all_098.append(counter_all_098.most_common(len(counter_all_098))[i][1])
        word_all_098.append(counter_all_098.most_common(len(counter_all_098))[i][0])

    for item in repeated_words_counter.items():
        repeated_words_count_297.append(counter_all_297[item[0]])
        repeated_words_count_098.append(counter_all_098[item[0]])
        repeated_words.append(item[0])

    print('bag-of-word similarity: ' + str(np.dot(np.array(repeated_words_count_297) / sum(count_all_297),
                                                  np.array(repeated_words_count_098) / sum(count_all_098))
                                           / (np.linalg.norm(
        np.array(count_all_297) / sum(count_all_297)) * np.linalg.norm(
        np.array(count_all_098) / sum(count_all_098)))))

    # calculate tf-idf vectors similarity
    tf_idf_vec_098 = tf_idf_vec_297 = [0.0] * len(repeated_words)

    for i in range(len(repeated_words)):
        tf_idf_vec_098[i] = tf_idf_all_098[word_all_098.index(repeated_words[i])]
        tf_idf_vec_297[i] = tf_idf_all_297[word_all_297.index(repeated_words[i])]

    print('tf-idf similarity: ' + str(np.dot(tf_idf_vec_098, tf_idf_vec_297) / (np.linalg.norm(tf_idf_all_297) * np.linalg.norm(tf_idf_all_098))))

