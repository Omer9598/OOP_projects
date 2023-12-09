username: omer_daniely

time and memory complexity of part A algorithm:

Time - O(n) in the first time calling the algorithm in a certain resolution
(the "render" command). After the first call, each call in the same resolution
will be in O(1) in average, because the number of available letters is
asymptotically constant.
In addition, the time complexity of checking that a key is in a map and
accessing it is O(1) in average.

Memory - O(n), because in the worst case where the number of letters in a row
is maximal (== number of pixels), we will save each sub image (== pixel) in the
cache map.