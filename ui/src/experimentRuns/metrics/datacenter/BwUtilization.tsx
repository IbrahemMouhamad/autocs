// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import NetworkCheckIcon from '@mui/icons-material/NetworkCheck';
import { useTranslate } from 'react-admin';

import { LinesChart, ChartContainer } from '../../../common';
import { groupHistoryByLabels, getMaxChartValue, getMinChartValue } from '../metricUtils';

const BwUtilization = ({ data }): JSX.Element | null => {
    const translate = useTranslate();
    const bwData = groupHistoryByLabels(
        data,
        'time',
        ['bwUtilization'],
        {
            bwUtilization: translate('label.run.metrics.bw.label'),
        },
    );
    const yMax = getMaxChartValue(bwData);
    const yMin = getMinChartValue(bwData);

    return (
        <ChartContainer
            headingText='label.run.metrics.bw.title'
            icon={NetworkCheckIcon}
        >
            <LinesChart
                data={bwData}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.bw.label')} (${translate('label.run.metrics.bw.unit')})`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

export default BwUtilization;
