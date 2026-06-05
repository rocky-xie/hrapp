import { beforeEach, describe, expect, it, vi } from 'vitest';

import axios from 'axios';
import dayjs from 'dayjs';

import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PositionMatch } from '@/shared/model/position-match.model';

import PositionMatchService from './position-match.service';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: vi.spyOn(axios, 'get'),
  post: vi.spyOn(axios, 'post'),
  put: vi.spyOn(axios, 'put'),
  patch: vi.spyOn(axios, 'patch'),
  delete: vi.spyOn(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('PositionMatch Service', () => {
    let service: PositionMatchService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PositionMatchService();
      currentDate = new Date();
      elemDefault = new PositionMatch(123, 0, 'AAAAAAA', 'AAAAAAA', 'IMMEDIATE', 'FIT', currentDate, 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { analysisDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        axiosStub.get.mockResolvedValue({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.mockRejectedValue(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a PositionMatch', async () => {
        const returnedFromService = { id: 123, analysisDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { analysisDate: currentDate, ...returnedFromService };

        axiosStub.post.mockResolvedValue({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PositionMatch', async () => {
        axiosStub.post.mockRejectedValue(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PositionMatch', async () => {
        const returnedFromService = {
          matchScore: 1,
          matchedSkills: 'BBBBBB',
          gapSkills: 'BBBBBB',
          readiness: 'BBBBBB',
          recommendation: 'BBBBBB',
          analysisDate: dayjs(currentDate).format(DATE_FORMAT),
          remark: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { analysisDate: currentDate, ...returnedFromService };
        axiosStub.put.mockResolvedValue({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PositionMatch', async () => {
        axiosStub.put.mockRejectedValue(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PositionMatch', async () => {
        const patchObject = {
          gapSkills: 'BBBBBB',
          readiness: 'BBBBBB',
          recommendation: 'BBBBBB',
          remark: 'BBBBBB',
          ...new PositionMatch(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { analysisDate: currentDate, ...returnedFromService };
        axiosStub.patch.mockResolvedValue({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PositionMatch', async () => {
        axiosStub.patch.mockRejectedValue(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PositionMatch', async () => {
        const returnedFromService = {
          matchScore: 1,
          matchedSkills: 'BBBBBB',
          gapSkills: 'BBBBBB',
          readiness: 'BBBBBB',
          recommendation: 'BBBBBB',
          analysisDate: dayjs(currentDate).format(DATE_FORMAT),
          remark: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { analysisDate: currentDate, ...returnedFromService };
        axiosStub.get.mockResolvedValue([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PositionMatch', async () => {
        axiosStub.get.mockRejectedValue(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PositionMatch', async () => {
        axiosStub.delete.mockResolvedValue({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PositionMatch', async () => {
        axiosStub.delete.mockRejectedValue(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
