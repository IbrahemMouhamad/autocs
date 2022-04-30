// Copyright (C) 2022 Ibrahem Mouhamad
//
// SPDX-License-Identifier: MIT

import React, { useMemo, Children } from 'react';
import get from 'lodash/get';
import clsx from 'clsx';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import { makeStyles } from '@mui/styles';
import Chip from '@mui/material/Chip';
import {
    TextField,
    BooleanField,
    FunctionField,
    Labeled,
} from 'react-admin';

import { filterObject } from '../utils';

import JsonFieldItem from './JsonFieldItem';

const useStyles = makeStyles(
    () => ({
        splitList: {
            columns: 2,
        },
        compact: {
            paddingBottom: 4,
            paddingTop: 4,
            minWidth: '230px',
        },
        label: {
            justifyContent: 'space-between',
            margin: 0,
            '&.RaLabeled-fullWidth': {
                flexDirection: 'row',
            },
            '& p': {
                margin: '8px 0 4px',
            },
        },
        field: {
            width: 'auto',
            margin: '0 8px',
        },
    }),
    {
        name: 'NLPJsonField',
    },
);

const JsonFieldLayout = (props): JSX.Element => {
    const {
        record,
        resource,
        children,
        splitList,
        className,
        ...rest
    } = props;
    const classes = useStyles();

    return (
        <List
            {...rest}
            className={splitList ?
                clsx(classes.compact, classes.splitList, className) :
                clsx(classes.compact, className)}
        >
            {Children.map(
                children,
                (child) => (
                    <ListItem key={`item-${child.props.source}`} className={classes.compact} divider>
                        <Labeled
                            label={child.props.label}
                            className={classes.label}
                            fullWidth
                        >
                            <JsonFieldItem
                                field={child}
                                record={record}
                                resource={resource}
                            />
                        </Labeled>
                    </ListItem>
                ),
            )}
        </List>
    );
};

const sanitizeJsonFieldProps = ({
    addLabel,
    ...props
}): {} => ({
    ...props,
});

const JsonField = (props): JSX.Element | null => {
    const {
        className,
        source = '',
        record,
        splitList,
        hide,
    } = props;
    const value = filterObject(get(record, source), hide);
    const classes = useStyles();

    return useMemo(
        () => (
            record ? (
                <JsonFieldLayout
                    {...sanitizeJsonFieldProps(props)}
                    className={className}
                    splitList={splitList}
                >
                    {
                        Object.keys(value).map((key) => {
                            const elementValue = get(record, `${source}.${key}`);
                            if (elementValue === true || elementValue === false) {
                                return (
                                    <BooleanField
                                        key={key}
                                        source={`${source}.${key}`}
                                        label={key}
                                        className={classes.field}
                                    />
                                );
                            }
                            if (Array.isArray(elementValue)) {
                                return (
                                    <FunctionField
                                        key={key}
                                        source={`${source}.${key}`}
                                        label={key}
                                        className={classes.field}
                                        render={() => (
                                            Object.keys(elementValue).map((item) => (
                                                <span key={item}>
                                                    <Chip label={elementValue[item]} />
                                                </span>
                                            ))
                                        )}
                                    />
                                );
                            }
                            return (
                                <TextField
                                    key={key}
                                    source={`${source}.${key}`}
                                    label={key}
                                    className={classes.field}
                                />
                            );
                        })
                    }
                </JsonFieldLayout>
            ) : null),
        [className, record, source],
    );
};

JsonField.defaultProps = {
    addLabel: true,
    hide: [],
};

export default JsonField;
