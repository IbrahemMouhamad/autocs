// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

/* eslint-disable @typescript-eslint/explicit-function-return-type */

export const getLineCharObject = (history, xKey, yKey, yLabel) => ({
    id: yLabel,
    data: history.map((item) => ({ x: item[xKey], y: item[yKey] })),
});

export const groupHistoryByLabels = (history, xKey, targetKeys, labels) => (
    history.length > 0 ? Object.keys(history[0]).filter((key) => key !== xKey)
        .filter((key) => targetKeys.includes(key))
        .map((key) => getLineCharObject(history, xKey, key, labels[key])) : []
);

export const calculateTimeToCpuPercentageSerie = (history, xKey, yLabel) => ({
    id: yLabel,
    data: history.map((item) => (
        { x: item[xKey], y: item.requestedMips > 0 ? item.allocatedMips / item.requestedMips : 0 })),
});

export const getMaxChartValue = (preppedData) => {
    const maxArray =
        preppedData.map((data) => data.data.reduce((max, p) => (p.y > max ? p.y : max), data.data[0].y));

    const max = Math.max(...maxArray);

    return (
        max * 1.2
    );
};

export const getMinChartValue = (preppedData) => {
    const maxArray =
        preppedData.map((data) => data.data.reduce((min, p) => (p.y < min ? p.y : min), data.data[0].y));

    const min = Math.min(...maxArray);

    return (
        Math.floor(min * 0.6)
    );
};
