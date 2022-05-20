// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React from 'react';
import PowerSettingsNewIcon from '@mui/icons-material/PowerSettingsNew';
import { useTranslate } from 'react-admin';

import { LinesChart, ChartContainer } from '../../../common';
import { groupHistoryByLabels, getMaxChartValue, getMinChartValue } from '../metricUtils';

const ActiveHostsNumber = ({ data }): JSX.Element | null => {
    const translate = useTranslate();
    const hostsData = groupHistoryByLabels(
        data,
        'time',
        ['activeHosts'],
        {
            activeHosts: translate('label.run.metrics.activeHosts.label'),
        },
    );
    const yMax = getMaxChartValue(hostsData);
    const yMin = getMinChartValue(hostsData);

    return (
        <ChartContainer
            headingText='label.run.metrics.activeHosts.title'
            icon={PowerSettingsNewIcon}
        >
            <LinesChart
                data={hostsData}
                xLegend={`${translate('label.run.metrics.time')} (${translate('label.run.metrics.time_unit')})`}
                yLegend={`${translate('label.run.metrics.activeHosts.unit')}${translate('label.run.metrics.activeHosts.label')}`}
                yMax={yMax}
                yMin={yMin}
            />
        </ChartContainer>
    );
};

export default ActiveHostsNumber;
