// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import MemoryIcon from '@mui/icons-material/Memory';
import { useTranslate } from 'react-admin';

import { LinesChart, ChartContainer } from '../../../common';
import { groupHistoryByLabels, getMaxChartValue, getMinChartValue } from '../metricUtils';

const RamUtilization = ({ data }): JSX.Element | null => {
    const translate = useTranslate();
    const ramData = groupHistoryByLabels(
        data,
        'time',
        ['ramUtilization'],
        {
            ramUtilization: translate('label.run.metrics.ram.label'),
        },
    );
    const yMax = getMaxChartValue(ramData);
    const yMin = getMinChartValue(ramData);

    return (
        <ChartContainer
            headingText='label.run.metrics.ram.title'
            icon={MemoryIcon}
        >
            <LinesChart
                data={ramData}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.ram.label')} (${translate('label.run.metrics.ram.unit')})`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

export default RamUtilization;
