package tetris;

public enum TileType {

    TypeI(0, new boolean[][] {
            {
                    false,	true,	false,	false,
                    false,	true,	false,	false,
                    false,	true,	false,	false,
                    false,	true,	false,	false,

            },
            {
                    false,	false,	false,	false,
                    true,	true,	true,	true,
                    false,	false,	false,	false,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	true,	false,
                    false,	false,	true,	false,
                    false,	false,	true,	false,
                    false,	false,	true,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	false,	false,	false,
                    true,	true,	true,	true,
                    false,	false,	false,	false,
            }
    }),

    TypeL(1, new boolean[][] {
            {
                    false,	true,	false,  false,
                    false,	true,	false,  false,
                    false,	true,	true,   false,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,	true,
                    false,	true,	false,	false,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,	false,
                    false,	false,	true,	false,
                    false,	false,	true,	false,
            },
            {
                    false,	false,	false,  false,
                    false,	false,	true,   false,
                    true,	true,	true,   false,
                    false,	false,	false,  false,
            }
    }),

    TypeJ(2, new boolean[][] {
            {
                    false,  false,	true,	false,
                    false,  false,	true,	false,
                    false,  true,	true,	false,
                    false,  false,  false,  false,
            },
            {
                    false,  false,  false,  false,
                    false,  true,	false,	false,
                    false,  true,	true,	true,
                    false,  false,	false,	false,
            },
            {
                    false,  false,  false,  false,
                    false,	true,	true,   false,
                    false,	true,	false,  false,
                    false,	true,	false,  false,
            },
            {
                    false,	false,	false,  false,
                    true,	true,	true,   false,
                    false,	false,	true,   false,
                    false,  false,  false,  false,
            }
    }),

    TypeS(3, new boolean[][] {
            {
                    false,	false,	false,	false,
                    false,	false,	true,	true,
                    false,	true,	true,	false,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	false,  false,
                    false,	true,	false,	false,
                    false,	true,	true,	false,
                    false,	false,	true,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,   false,
                    true,	true,	false,  false,
                    false,	false,	false,  false,
            },
            {
                    false,	true,	false,  false,
                    false,	true,	true,   false,
                    false,	false,	true,   false,
                    false,	false,	false,	false,
            }
    }),

    TypeZ(4, new boolean[][] {
            {
                    false,	false,	false,	false,
                    false,	true,	true,	false,
                    false,	false,	true,	true,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	false,	true,	false,
                    false,	true,	true,	false,
                    false,	true,	false,	false,
            },
            {
                    false,	false,	false,	false,
                    true,	true,	false,  false,
                    false,	true,	true,   false,
                    false,	false,	false,  false,
            },
            {
                    false,	false,	true,   false,
                    false,	true,	true,   false,
                    false,	true,	false,  false,
                    false,	false,	false,	false,
            }
    }),

    TypeO(5, new boolean[][] {
            {
                    false,	false,	false,	false,
                    false,	true,	true,   false,
                    false,	true,	true,   false,
                    false,  false,  false,  false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,   false,
                    false,	true,	true,   false,
                    false,  false,  false,  false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,   false,
                    false,	true,	true,   false,
                    false,  false,  false,  false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	true,   false,
                    false,	true,	true,   false,
                    false,  false,  false,  false,
            }
    }),

    TypeT(6, new boolean[][] {
            {
                    false,	false,	false,	false,
                    false,	true,	true,	true,
                    false,	false,	true,	false,
                    false,	false,	false,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	false,	true,	false,
                    false,	true,	true,	false,
                    false,	false,	true,	false,
            },
            {
                    false,	false,	false,	false,
                    false,	true,	false,  false,
                    true,	true,	true,   false,
                    false,	false,	false,  false,
            },
            {
                    false,	true,	false,  false,
                    false,	true,	true,   false,
                    false,	true,	false,  false,
                    false,	false,	false,	false,
            }
    });

    /**
     * The column that this type spawns in.
     */
    private int spawnCol;

    /**
     * The row that this type spawns in.
     */
    private int spawnRow;
    private boolean[][] tiles;
    private int no;

    private TileType(int no, boolean[][] tiles) {
        this.tiles = tiles;
        this.no = no;

        this.spawnCol = 5 - (4 >> 1);
        this.spawnRow = getTopInset(0);
    }

    public int getNo(){
        return no;
    }


    /**
     * Gets the spawn column of this type.
     * @return The spawn column.
     */
    public int getSpawnColumn() {
        return spawnCol;
    }

    /**
     * Gets the spawn row of this type.
     * @return The spawn row.
     */
    public int getSpawnRow() {
        return spawnRow;
    }

    public boolean isTile(int x, int y, int rotation) {
        return tiles[rotation][y * 4 + x];
    }

    /**
     * The left inset is represented by the number of empty columns on the left
     * side of the array for the given rotation.
     * @param rotation The rotation.
     * @return The left inset.
     */
    public int getLeftInset(int rotation) {
		/*
		 * Loop through from left to right until we find a tile then return
		 * the column.
		 */
        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                if(isTile(x, y, rotation)) {
                    return x;
                }
            }
        }
        return -1;
    }

    /**
     * The right inset is represented by the number of empty columns on the left
     * side of the array for the given rotation.
     * @param rotation The rotation.
     * @return The right inset.
     */
    public int getRightInset(int rotation) {
		/*
		 * Loop through from right to left until we find a tile then return
		 * the column.
		 */
        for(int x = 3; x >= 0; x--) {
            for(int y = 0; y < 4; y++) {
                if(isTile(x, y, rotation)) {
                    return 4 - x;
                }
            }
        }
        return -1;
    }

    /**
     * The left inset is represented by the number of empty rows on the top
     * side of the array for the given rotation.
     * @param rotation The rotation.
     * @return The top inset.
     */
    public int getTopInset(int rotation) {
		/*
		 * Loop through from top to bottom until we find a tile then return
		 * the row.
		 */
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(isTile(x, y, rotation)) {
                    return y;
                }
            }
        }
        return -1;
    }

    /**
     * The botom inset is represented by the number of empty rows on the bottom
     * side of the array for the given rotation.
     * @param rotation The rotation.
     * @return The bottom inset.
     */
    public int getBottomInset(int rotation) {
		/*
		 * Loop through from bottom to top until we find a tile then return
		 * the row.
		 */
        for(int y = 3; y >= 0; y--) {
            for(int x = 0; x < 4; x++) {
                if(isTile(x, y, rotation)) {
                    return 4 - y;
                }
            }
        }
        return -1;
    }

}
