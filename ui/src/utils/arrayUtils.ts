// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

const filterObject = (source, remove): object => (
    Object.keys(source)
        .filter((key) => !remove.includes(key))
        .reduce((obj, key) => ({
            ...obj,
            [key]: source[key],
        }), {})
);

export {
    filterObject,
};
